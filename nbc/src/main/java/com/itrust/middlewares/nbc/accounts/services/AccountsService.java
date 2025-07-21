package com.itrust.middlewares.nbc.accounts.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.accounts.dtos.requests.*;
import com.itrust.middlewares.nbc.accounts.dtos.responses.AccountEnquiryResponseDTO;
import com.itrust.middlewares.nbc.accounts.dtos.responses.AccountMiniStatementResponseDTO;
import com.itrust.middlewares.nbc.accounts.dtos.responses.AccountStatusResponseDTO;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * Service class for handling account-related operations.
 * This service provides functionality for account opening, status checking,
 * statement retrieval, and account inquiries by interacting with the NBC backend systems.
 * 
 * @author iTrust Middleware Team
 * @version 1.0
 */
@Service
public class AccountsService extends BaseService {

    private final ProducerTemplate producerTemplate;

    /** Base endpoint for NBC account operations */
    @Value("${base_end_point}")
    private String baseEndPoint;

    /** Endpoint for statement-related operations */
    @Value("${statements_end_point}")
    private String baseUtilityEndPoint;

    /** Callback URL for an account opening process */
    @Value("${open.account.callback.url}")
    private String callbackUrl;

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;

    /**
     * Constructor for AccountsService.
     * 
     * @param securityUtils Security utilities for encryption/decryption
     * @param restTemplate REST template for making HTTP requests
     * @param producerTemplate Camel producer template for messaging
     */
    public AccountsService(SecurityUtils securityUtils, RestTemplate restTemplate,  ProducerTemplate producerTemplate) {
        
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        this.producerTemplate = producerTemplate;
        logInfo("AccountsService initialized");
    }

    /**
     * Opens a new account based on the provided request data.
     * This method processes the account opening request, transforms it to match NBC's requirements,
     * sends it to the NBC backend, and processes the response.
     * 
     * @param accountOpenRequestDTO The request containing account opening details
     * @return RestResponse containing the result of the account opening operation
     */
    public RestResponse openAccount(AccountOpenRequestDTO accountOpenRequestDTO) {
        logInfo("Processing account opening request for NIN: " + accountOpenRequestDTO.getNin());

        try {

            // Modify request Object to fit NBC request payload
            logDebug("Transforming account opening request to match NBC requirements");
            accountOpenRequestDTO.setCallBackUrl(callbackUrl);
            accountOpenRequestDTO.setDesignation("INDIVIDUAL");
            accountOpenRequestDTO.setOtpVerified(true);
            accountOpenRequestDTO.setCustomerInvitedThrough("iTrust Finance");

            List<String> productCode = new ArrayList<>();
            productCode.add("102");
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

            // Create NBC-specific request DTO
            AccountOpenNBCRequestDTO accountOpenNBCRequestDTO = new AccountOpenNBCRequestDTO();
            BeanUtils.copyProperties(accountOpenRequestDTO, accountOpenNBCRequestDTO);

            // Send a request to NBC
            logInfo("Sending account opening request to NBC endpoint: " + baseEndPoint + "/account/open");
            String results = restTemplate.postForObject(baseEndPoint + "/account/open", accountOpenNBCRequestDTO, String.class);
            logDebug("Received response from NBC for account opening request");


            if (results != null && !results.isEmpty()) {
                logDebug("Parsing NBC response for account opening request");
                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("NBC response for account opening: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Account opening request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Account opening request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received from NBC for account opening request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), 
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during account opening: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

    /**
     * Checks the status of an account based on the provided ID number.
     * This method sends a request to the NBC backend to retrieve account status information.
     * 
     * @param accountStatusRequestDTO The request containing the ID number for status check
     * @return RestResponse containing the account status information
     */
    public RestResponse accountStatus(AccountStatusRequestDTO accountStatusRequestDTO) {
        logInfo("Processing account status request for ID number: " + accountStatusRequestDTO.getIdNumber());

        try {
            // Encrypt payload for security
            logDebug("Encrypting account status request payload");
            String encryptedPayload = securityUtils.encrypt(accountStatusRequestDTO.toString());

            // Send a request to NBC
            logInfo("Sending account status request to NBC endpoint: " + baseEndPoint + "/account/status");
            String results = restTemplate.postForObject(baseEndPoint + "/account/status", accountStatusRequestDTO, String.class);
            logDebug("Received response from NBC for account status request");

            if (results != null && !results.isEmpty()) {
                logDebug("Decrypting and parsing NBC response for account status request");
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                AccountStatusResponseDTO responseDTO = mapper.readValue(results, AccountStatusResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("NBC response for account status: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Account status request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Account status request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received from NBC for account status request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during account status check: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

    /**
     * Retrieves a mini statement for an account based on the provided account number and date range.
     * This method sends a request to the appropriate endpoint based on the source parameter.
     * 
     * @param requestDTO The request containing account number and date range for the mini statement
     * @param source The source of the request (e.g., "itrust")
     * @return RestResponse containing the mini statement information
     */
    public RestResponse accountMiniStatement(AccountMiniStatementRequestDTO requestDTO, String source) {
        logInfo("Processing mini statement request for account: " + requestDTO.getAccountNumber() + " from source: " + source);

        try {
            // Encrypt payload for security
            logDebug("Encrypting mini statement request payload");
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());

            // Send a request to the appropriate endpoint based on source
            String results;
            if (source.equals("itrust")) {
                logInfo("Using Camel route for iTrust source: direct:cbs-mini-statement");
                results = producerTemplate.requestBody("direct:cbs-mini-statement", requestDTO, String.class);
            } else {
                logInfo("Using REST endpoint for source " + source + ": " + baseUtilityEndPoint + "/account/balance");
                results = restTemplate.postForObject(baseUtilityEndPoint + "/account/balance", requestDTO, String.class);
            }

            logDebug("Received response for mini statement request: " + results);

            if (results != null && !results.isEmpty()) {
                logDebug("Decrypting and parsing response for mini statement request");
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                AccountMiniStatementResponseDTO responseDTO = mapper.readValue(results, AccountMiniStatementResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("Response for mini statement: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Mini statement request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Mini statement request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received for mini statement request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during mini statement retrieval: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

    /**
     * Retrieves a full statement for an account based on the provided account number and date range.
     * This method sends a request to the NBC backend to retrieve the full statement information.
     * 
     * @param requestDTO The request containing account number and date range for the full statement
     * @return RestResponse containing the full statement information
     */
    public RestResponse accountFullStatement(AccountFullStatementRequestDTO requestDTO) {
        logInfo("Processing full statement request for account: " + requestDTO.getAccountNumber());

        try {
            // Encrypt payload for security
            logDebug("Encrypting full statement request payload");
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());

            // Send a request to NBC
            logInfo("Sending full statement request to endpoint: " + baseUtilityEndPoint + "/account/statement/full");
            String results = restTemplate.postForObject(baseUtilityEndPoint + "/account/statement/full", requestDTO, String.class);
            logDebug("Received response for full statement request: " + results);

            if (results != null && !results.isEmpty()) {
                logDebug("Decrypting and parsing response for full statement request");
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("Response for full statement: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Full statement request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Full statement request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received for full statement request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during full statement retrieval: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

    /**
     * Performs an account inquiry/balance check for the specified account number.
     * This method sends a request to the NBC backend to retrieve account balance information.
     * 
     * @param accountEnquiryDTO The request containing the account number for inquiry
     * @return RestResponse containing the account balance information
     */
    public RestResponse accountEnquiry(AccountEnquiryRequestDTO accountEnquiryDTO) {
        logInfo("Processing account inquiry request for account: " + accountEnquiryDTO.getAccountNumber());

        try {
            // Encrypt payload for security
            logDebug("Encrypting account inquiry request payload");
            String encryptedPayload = securityUtils.encrypt(accountEnquiryDTO.toString());

            // Send a request to NBC
            logInfo("Sending account inquiry request to endpoint: " + baseUtilityEndPoint + "/account/balance");
            String results = restTemplate.postForObject(baseUtilityEndPoint + "/account/balance", accountEnquiryDTO, String.class);
            logDebug("Received response for account inquiry request: " + results);

            if (results != null && !results.isEmpty()) {
                logDebug("Decrypting and parsing response for account inquiry request");
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                AccountStatusResponseDTO responseDTO = mapper.readValue(results, AccountStatusResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("Response for account inquiry: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Account inquiry request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Account inquiry request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received for account inquiry request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during account inquiry: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

    /**
     * Retrieves available account options from the NBC backend.
     * This method sends a request to the NBC backend to get a list of available account options.
     * 
     * @return RestResponse containing the available account options
     */
    public RestResponse options() {
        logInfo("Processing request for account options");

        try {
            // Encrypt an empty payload for security
            logDebug("Encrypting empty payload for options request");
            String encryptedPayload = securityUtils.encrypt("");

            // Send a request to NBC
            logInfo("Sending options request to endpoint: " + baseEndPoint + "/options");
            String results = restTemplate.getForObject(baseEndPoint + "/options", String.class);
            logDebug("Received response for options request: " + results);

            if (results != null && !results.isEmpty()) {
                logDebug("Decrypting and parsing response for options request");
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                AccountEnquiryResponseDTO responseDTO = mapper.readValue(results, AccountEnquiryResponseDTO.class);

                // Get status and message from response
                String responseCode = responseDTO.getStatusCode();
                String responseMessage = responseDTO.getMessage();
                logInfo("Response for options request: code=" + responseCode + ", message=" + responseMessage);

                Map<String, Object> messages = new HashMap<>();

                if (Objects.equals(responseCode, "600")) {
                    logInfo("Options request successful");
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Options request failed with code: " + responseCode);
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
                }
            } else {
                logError("Empty response received for options request");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during options retrieval: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponse(e, null);
        }
    }

}
