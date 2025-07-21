package com.itrust.middlewares.nbc.onboarding.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.accounts.dtos.callback.AccountOpenCallbackRequestDTO;
import com.itrust.middlewares.nbc.accounts.dtos.requests.AccountOpenNBCRequestDTO;
import com.itrust.middlewares.nbc.accounts.dtos.requests.AccountOpenRequestDTO;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationDataEntity;
import com.itrust.middlewares.nbc.kycValidation.repository.KycValidationDataRepository;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.onboarding.repository.OnboardingRepository;
import com.itrust.middlewares.nbc.onboarding.entities.AccountEntity;
import com.itrust.middlewares.nbc.onboarding.requests.AccountDetailsDTO;
import com.itrust.middlewares.nbc.onboarding.requests.AccountSyncDTO;
import com.itrust.middlewares.nbc.onboarding.requests.CreateAccountDTO;
import com.itrust.middlewares.nbc.onboarding.requests.CreateAccountRetryDTO;
import com.itrust.middlewares.nbc.onboarding.responses.AccountDTO;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import jakarta.validation.Valid;
import org.apache.camel.ProducerTemplate;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service("AccountOnboardService")
public class OnboardingService extends BaseService {

    private final TransactionTemplate transactionTemplate;
    @Value("${base_end_point}")
    private String baseEndPoint;

    @Value("${open.account.callback.url}")
    private String callbackUrl;

    private final RestTemplate restTemplate;
    private final OnboardingRepository onboardingRepository;
    private final KycValidationDataRepository kycValidationDataRepository;
    private final SecurityUtils securityUtils;
    private final ProducerTemplate producerTemplate;

    public OnboardingService(RestTemplate restTemplate, OnboardingRepository onboardingRepository, SecurityUtils securityUtils, ProducerTemplate producerTemplate, TransactionTemplate transactionTemplate, KycValidationDataRepository kycValidationDataRepository) {
        
        this.restTemplate = restTemplate;
        this.onboardingRepository = onboardingRepository;
        this.securityUtils = securityUtils;
        this.producerTemplate = producerTemplate;
        this.transactionTemplate = transactionTemplate;
        this.kycValidationDataRepository = kycValidationDataRepository;
    }


/**
 * Creates a new account based on the provided CreateAccountDTO.
 *
 * @param createAccountDTO the account creation request data
 * @param source the source of the request
 * @return a RestResponse indicating the result of the account creation process
 */

    public RestResponse createAccount(CreateAccountDTO createAccountDTO, String source) {
        try {

            logger.info("create account start");
            // look for existing account by NIN
            Optional<AccountEntity> accountCheck = onboardingRepository.findByNin(createAccountDTO.getNin());

            // Initialize Account Entity for Save later
            AccountEntity saveAccount;

            if (accountCheck.isPresent()) {
                AccountEntity account = accountCheck.get();
                account.setCopReference(createAccountDTO.getCopReference());
                mapRequestAndAccount(createAccountDTO, account);
                saveAccount = account;
            } else {
                saveAccount = new AccountEntity();
                saveAccount.setStatus("new");
                saveAccount.setNin(createAccountDTO.getNin());
                mapRequestAndAccount(createAccountDTO, saveAccount);
            }

            // the investor has no account start account creation process

            // modify request Object to fit NBC request payload
            ObjectMapper mapper = new ObjectMapper();
            AccountOpenRequestDTO accountOpenRequestDTO = mapOpenAccountRequestAndAccount(createAccountDTO);


            accountOpenRequestDTO.setCallBackUrl(callbackUrl);
            accountOpenRequestDTO.setOtpVerified(true);
            accountOpenRequestDTO.setCustomerInvitedThrough("iTrust Finance");
            List<String> productCode = new ArrayList<>();
            if (createAccountDTO.getProductCode() != null) {
                productCode.add(createAccountDTO.getProductCode());
            } else {
                productCode.add("102");
            }

            accountOpenRequestDTO.setProductcode(productCode);
            List<String> sourceOfFund = new ArrayList<>();
            if (createAccountDTO.getSourceOfFund() != null) {
                sourceOfFund.add(createAccountDTO.getSourceOfFund());
            } else {
                sourceOfFund.add("INCOME");
            }
            accountOpenRequestDTO.setSourceofFund(sourceOfFund);
            accountOpenRequestDTO.setTransactcountries(new ArrayList<>());
            accountOpenRequestDTO.setInternationalTrans("N");
            accountOpenRequestDTO.setBranchCode("011");
            accountOpenRequestDTO.setOccupation("0");
            accountOpenRequestDTO.setEmployer("");
            accountOpenRequestDTO.setEmployerIndustry("");
            accountOpenRequestDTO.setEmployerAddress("");
            accountOpenRequestDTO.setEducationLevel("");
            accountOpenRequestDTO.setJobposition("");
            accountOpenRequestDTO.setSalesCode("");

            AccountOpenNBCRequestDTO accountOpenNBCRequestDTO = new AccountOpenNBCRequestDTO();
            BeanUtils.copyProperties(accountOpenRequestDTO, accountOpenNBCRequestDTO);

            // Encrypt payload
            String encryptedPayload = securityUtils.encrypt(accountOpenNBCRequestDTO.toString());
            String results;
            if (source.equals("itrust")) {
                logger.info("create account start routing to mock");
                results = producerTemplate.requestBody("direct:onboarding-create-account", accountOpenNBCRequestDTO, String.class);
             } else {
                results = restTemplate.postForObject(baseEndPoint + "/account/open", accountOpenNBCRequestDTO, String.class);
            }

            if (results != null && !results.isEmpty()) {
                String response = securityUtils.decrypt(results);

                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {

                    transactionTemplate.execute(status -> {
                        onboardingRepository.updateAfterNBC(saveAccount.getNin(), "pending", responseCode, responseMessage);
                        onboardingRepository.save(saveAccount);
                        return null;
                    });

                    return successResponse(responseMessage, responseCode, null, responseDTO.getData());
                } else {
                    if (responseDTO.getErrors() != null) {
                        for (Map<String, Object> error : responseDTO.getErrors()) {
                            messages.put((String) error.get("field"), error.get("message"));
                        }
                    }
                    transactionTemplate.execute(status -> {
                        onboardingRepository.updateAfterNBC(saveAccount.getNin(), "failed", responseCode, responseMessage);
                        onboardingRepository.save(saveAccount);
                        return null;
                    });

                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }

            } else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages, null);
            }

        } catch (Exception e) {
            return exceptionResponse(e, null);
        }
    }



/**
 * Creates a new account based on the provided CreateAccountDTO.
 *
 * @param createAccountDTO the account creation request data
 * @param source the source of the request
 * @return a RestResponse indicating the result of the account creation process
 */

    public RestResponse createAccountRetry(CreateAccountRetryDTO createAccountDTO, String source) {
        try {

            // look for existing account by NIN
            Optional<AccountEntity> accountCheck = onboardingRepository.findByNin(createAccountDTO.getNin());

            // Initialize Account Entity for Save later
            AccountEntity saveAccount;

            if (accountCheck.isEmpty()) {
                return errorResponse("No recent validation found", ResponseCode.VALIDATION_FAILED.getCode(), null, null);
            }

            saveAccount = accountCheck.get();

            // investor has no account start account creation process

            // modify request Object to fit NBC request payload
            ObjectMapper mapper = new ObjectMapper();
            AccountOpenRequestDTO accountOpenRequestDTO = new AccountOpenRequestDTO();
            BeanUtils.copyProperties(createAccountDTO, accountOpenRequestDTO);

            accountOpenRequestDTO.setCallBackUrl(callbackUrl);
            accountOpenRequestDTO.setOtpVerified(true);
            accountOpenRequestDTO.setCustomerInvitedThrough("iTrust Finance");
            List<String> productCode = new ArrayList<>();
            if (createAccountDTO.getProductCode() != null) {
                productCode.add(createAccountDTO.getProductCode());
            } else {
                productCode.add("102");
            }

            accountOpenRequestDTO.setProductcode(productCode);
            List<String> sourceOfFund = new ArrayList<>();
            sourceOfFund.add("INCOME");
            accountOpenRequestDTO.setSourceofFund(sourceOfFund);
            accountOpenRequestDTO.setTransactcountries(new ArrayList<>());
            accountOpenRequestDTO.setInternationalTrans("N");
            accountOpenRequestDTO.setBranchCode("011");
            accountOpenRequestDTO.setOccupation("0");
            accountOpenRequestDTO.setEmployer("");
            accountOpenRequestDTO.setEmployerIndustry("");
            accountOpenRequestDTO.setEmployerAddress("");
            accountOpenRequestDTO.setEducationLevel("");
            accountOpenRequestDTO.setJobposition("");
            accountOpenRequestDTO.setSalesCode("");

            AccountOpenNBCRequestDTO accountOpenNBCRequestDTO = new AccountOpenNBCRequestDTO();
            BeanUtils.copyProperties(accountOpenRequestDTO, accountOpenNBCRequestDTO);

            // Encrypt payload
            String encryptedPayload = securityUtils.encrypt(accountOpenNBCRequestDTO.toString());
            String results;
            if (source.equals("itrust")) {
                results = producerTemplate.requestBody("direct:onboarding-create-account-retry", accountOpenNBCRequestDTO, String.class);
             } else {
                results = restTemplate.postForObject(baseEndPoint + "/account/open", accountOpenNBCRequestDTO, String.class);
            }

            if (results != null && !results.isEmpty()) {
                String response = securityUtils.decrypt(results);


                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {

                    transactionTemplate.execute(status -> {
                        onboardingRepository.updateAfterNBC(saveAccount.getNin(), "pending", responseCode, responseMessage);
                        onboardingRepository.save(saveAccount);
                        return null;
                    });

                    return successResponse(responseMessage, responseCode, null, responseDTO.getData());
                } else {
                    if (responseDTO.getErrors() != null) {
                        for (Map<String, Object> error : responseDTO.getErrors()) {
                            messages.put((String) error.get("field"), error.get("message"));
                        }
                    }
                    transactionTemplate.execute(status -> {
                        onboardingRepository.updateAfterNBC(saveAccount.getNin(), "failed", responseCode, responseMessage);
                        onboardingRepository.save(saveAccount);
                        return null;
                    });
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }

            } else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages, null);
            }

        } catch (Exception e) {
            return exceptionResponse(e, null);
        }
    }

    /**
     * Maps the fields from CreateAccountDTO to AccountEntity.
     *
     * @param createAccountDTO the account creation request data
     */
    private AccountOpenRequestDTO mapOpenAccountRequestAndAccount(@NotNull CreateAccountDTO createAccountDTO) {
        AccountOpenRequestDTO account = new AccountOpenRequestDTO();
        account.setNin(createAccountDTO.getNin());
        account.setDesignation(createAccountDTO.getDesignation());
        account.setOthernames(createAccountDTO.getOtherNames());
        account.setShortname(createAccountDTO.getShortName());
        account.setCountryofResidence(createAccountDTO.getCountryOfResidence());
        account.setCurrentPhonenumber(createAccountDTO.getCurrentPhoneNumber());
        account.setResidentPostaladdress(createAccountDTO.getResidentPostalAddress());
        account.setBirthcountry(createAccountDTO.getBirthCountry());
        account.setEmail(createAccountDTO.getEmail());
        account.setTitle(createAccountDTO.getTitle());
        account.setMonthlyIncome(createAccountDTO.getMonthlyIncome());
        account.setEducationLevel(createAccountDTO.getEducationLevel());
        account.setSpouseName(createAccountDTO.getSpouseName());
        account.setSpousePhone(createAccountDTO.getSpousePhone());
        account.setFlgPep(createAccountDTO.getFlgPep());
        account.setLanguage(createAccountDTO.getLanguage());
        account.setTinNumber(createAccountDTO.getTinNumber());
        account.setNationality(createAccountDTO.getNationality());
        account.setRegion(createAccountDTO.getRegion());
        account.setDistrict(createAccountDTO.getDistrict());
        account.setWard(createAccountDTO.getWard());
        account.setPlaceBirth(createAccountDTO.getPlaceBirth());
        account.setBankAccountNumber(createAccountDTO.getBankAccountNumber());
        account.setBankAccountName(createAccountDTO.getBankAccountName());
        account.setBankBranch(createAccountDTO.getBankBranch());
        account.setBankName(createAccountDTO.getBankName());
        account.setEmploymentStatus(createAccountDTO.getEmploymentStatus());
        account.setOtherBusiness(createAccountDTO.getOtherBusiness());
        account.setBusinessSector(createAccountDTO.getBusinessSector());
        account.setEmployerName(createAccountDTO.getEmployerName());
        account.setOtherEmployment(createAccountDTO.getOtherEmployment());
        account.setCurrentOccupation(createAccountDTO.getCurrentOccupation());
        account.setSourceOfIncome(createAccountDTO.getSourceOfIncome());
        account.setIncomeFrequency(createAccountDTO.getIncomeFrequency());
        account.setKinName(createAccountDTO.getKinName());
        account.setKinMobile(createAccountDTO.getKinMobile());
        account.setKinEmail(createAccountDTO.getKinEmail());
        account.setKinRelationship(createAccountDTO.getKinRelationship());
        account.setCopReference(createAccountDTO.getCopReference());
        account.setOtp(createAccountDTO.getOtp());
        return account;
    }


    /**
     * Maps the fields from CreateAccountDTO to AccountEntity.
     *
     * @param createAccountDTO the account creation request data
     * @param account the account entity to be updated
     */
    private void mapRequestAndAccount(@NotNull CreateAccountDTO createAccountDTO, @NotNull AccountEntity account) {
        account.setFirstName(createAccountDTO.getFirstName());
        account.setMiddleName(createAccountDTO.getMiddleName());
        account.setLastName(createAccountDTO.getSurName());
        account.setSex(createAccountDTO.getSex());
        account.setOthernames(createAccountDTO.getOtherNames());
        account.setShortname(createAccountDTO.getShortName());
        account.setCountryofResidence(createAccountDTO.getCountryOfResidence());
        account.setCurrentPhonenumber(createAccountDTO.getCurrentPhoneNumber());
        account.setResidentPostaladdress(createAccountDTO.getResidentPostalAddress());
        account.setBirthcountry(createAccountDTO.getBirthCountry());
        account.setEmail(createAccountDTO.getEmail());
        account.setTitle(createAccountDTO.getTitle());
        account.setMonthlyIncome(createAccountDTO.getMonthlyIncome());
        account.setEducationLevel(createAccountDTO.getEducationLevel());
        account.setSpouseName(createAccountDTO.getSpouseName());
        account.setSpousePhone(createAccountDTO.getSpousePhone());
        account.setFlgPep(createAccountDTO.getFlgPep());
        account.setLanguage(createAccountDTO.getLanguage());
        account.setTinNumber(createAccountDTO.getTinNumber());
        account.setNationality(createAccountDTO.getNationality());
        account.setRegion(createAccountDTO.getRegion());
        account.setDistrict(createAccountDTO.getDistrict());
        account.setWard(createAccountDTO.getWard());
        account.setPlaceBirth(createAccountDTO.getPlaceBirth());
        account.setBankAccountNumber(createAccountDTO.getBankAccountNumber());
        account.setBankAccountName(createAccountDTO.getBankAccountName());
        account.setBankBranch(createAccountDTO.getBankBranch());
        account.setBankName(createAccountDTO.getBankName());
        account.setEmploymentStatus(createAccountDTO.getEmploymentStatus());
        account.setOtherBusiness(createAccountDTO.getOtherBusiness());
        account.setBusinessSector(createAccountDTO.getBusinessSector());
        account.setEmployerName(createAccountDTO.getEmployerName());
        account.setOtherEmployment(createAccountDTO.getOtherEmployment());
        account.setCurrentOccupation(createAccountDTO.getCurrentOccupation());
        account.setSourceOfIncome(createAccountDTO.getSourceOfIncome());
        account.setIncomeFrequency(createAccountDTO.getIncomeFrequency());
        account.setKinName(createAccountDTO.getKinName());
        account.setKinMobile(createAccountDTO.getKinMobile());
        account.setKinEmail(createAccountDTO.getKinEmail());
        account.setKinRelationship(createAccountDTO.getKinRelationship());
        account.setCopReference(createAccountDTO.getCopReference());
        account.setArthaReference(createAccountDTO.getArtthaReference());
        account.setNin(createAccountDTO.getNin());

        onboardingRepository.save(account);
    }


    /**
     * Handles the callback for account creation.
     *
     * @param requestDTO the callback request data
     * @return a RestResponse indicating the result of the callback processing
     */
    public RestResponse createAccountCallback(@Valid AccountOpenCallbackRequestDTO requestDTO) {
        try {
            AccountEntity account = onboardingRepository.findByCopReference(requestDTO.getBody().getData().getCopreference());
            if (account != null) {
                account.setCustomerId(requestDTO.getBody().getData().getCustomerId());
                account.setAccountId(requestDTO.getBody().getData().getNewAccountId());
                account.setAccountStatus(requestDTO.getBody().getData().getAccountStatus());
                account.setStatus("created");

                transactionTemplate.execute(status -> {
                            onboardingRepository.save(account);
                            return null;
                        });
                
                createBrokerageAccount(account, requestDTO.getBody().getData().getNewAccountId(), requestDTO.getBody().getData().getCustomerId());

                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.CLIENT_NOT_REGISTERED.getCode(), null, requestDTO);
            } else {
                return errorResponse(ResponseCode.NO_DATA_FOUND_ON_DB.getMessage(), ResponseCode.NO_DATA_FOUND_ON_DB.getCode(), null, null);
            }
        } catch (Exception e) {
            return exceptionResponse(e, null);
        }
    }

/**
 * Asynchronously creates a brokerage account.
 *
 * @param account the account entity
 * @param newAccountId the new account ID
 * @param customerId the customer ID
 */
    @Async
    protected void createBrokerageAccount(AccountEntity account, String newAccountId, String customerId) {
        try {
            KycValidationDataEntity kycValidationDataEntity = kycValidationDataRepository.findFirstByNin(account.getNin());
            logger.info("brokerage callback creator  {}", account);
            account.setAccountId(newAccountId);
            account.setCustomerId(customerId);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            ObjectNode jsonNode = mapper.valueToTree(account);
            jsonNode.remove("id");
            jsonNode.remove("createdAt");
            jsonNode.remove("updatedAt");
            if(kycValidationDataEntity != null) {
                jsonNode.put("dob", kycValidationDataEntity.getDateOfBirth());
            }

           String response = producerTemplate.requestBody("direct:brokerage-create-account", jsonNode, String.class);
            logger.info("brokerage callback response  {}", response);
        } catch (Exception e) {
            logSlack("Failed to create brokerage account for " + account.getNin());
        }
    }


    /**
     * Synchronizes account data.
     *
     * @param accountSyncDTO the account synchronization data
     * @return a RestResponse indicating the result of the synchronization process
     */
    public RestResponse accountSyncSyncing(@Valid AccountSyncDTO accountSyncDTO) {
        try {
            AccountEntity accountEntity = onboardingRepository.findFirstByNin(accountSyncDTO.getNin());

                if (accountEntity != null) {
                    accountEntity.setDseCds(accountSyncDTO.getDseCds());
                    accountEntity.setDseCdsStatus(accountSyncDTO.getDseCdsStatus());
                    accountEntity.setInnovaClientCode(accountSyncDTO.getInnovaClientCode());
                    accountEntity.setInnovaClientCodeStatus(accountSyncDTO.getInnovaClientCodeStatus());
                    accountEntity.setCbsCif(accountSyncDTO.getCbsCif());
                    accountEntity.setCbsCifStatus(accountSyncDTO.getCbsCifStatus());
                    accountEntity.setCbsKyc(accountSyncDTO.getCbsKyc());
                    accountEntity.setCbsKycStatus(accountSyncDTO.getCbsKycStatus());
                    accountEntity.setCbsAcc(accountSyncDTO.getCbsAcc());
                    accountEntity.setCbsAccStatus(accountSyncDTO.getCbsAccStatus());
                onboardingRepository.save(accountEntity);
                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null, accountEntity);
            } else {
                return errorResponse(ResponseCode.NO_DATA_FOUND_ON_DB.getMessage(), ResponseCode.NO_DATA_FOUND_ON_DB.getCode(), null, null);
            }
        } catch (Exception e) {
            return exceptionResponse(e, null);
        }
    }

    
    /**
     * Retrieves account details based on the provided request data.
     *
     * @param requestDTO the account details request data
     * @return a RestResponse containing the account details
     */
    public RestResponse accountDetails(@Valid AccountDetailsDTO requestDTO) {
        try {
            AccountEntity accountEntity = onboardingRepository.findFirstByNin(requestDTO.getNin());

            if (accountEntity != null) {
                ModelMapper mapper = new ModelMapper();
                AccountDTO accountDTO = mapper.map(accountEntity, AccountDTO.class);
                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null, accountDTO);
            } else {
                return errorResponse(ResponseCode.NO_DATA_FOUND_ON_DB.getMessage(), ResponseCode.NO_DATA_FOUND_ON_DB.getCode(), null, null);
            }
        } catch (Exception e) {
            return exceptionResponse(e, null);
        }
    }


    
    /**
     * Updates the retry count for the account.
     *
     * @param account the account entity
     * @param nin the NIN of the account
     */
    private void updateRetry(AccountEntity account, String nin) {
        if (account == null) {
            onboardingRepository.updateRetry(nin, 1);
        } else {
            onboardingRepository.updateRetry(nin, account.getRetries() + 1);
        }
    }

}

