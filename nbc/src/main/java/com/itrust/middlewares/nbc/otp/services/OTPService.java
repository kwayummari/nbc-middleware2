package com.itrust.middlewares.nbc.otp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.onboarding.entities.AccountEntity;
import com.itrust.middlewares.nbc.onboarding.repository.OnboardingRepository;
import com.itrust.middlewares.nbc.otp.dto.requests.OTPSendRequestDTO;
import com.itrust.middlewares.nbc.otp.dto.requests.OTPVerifyRequestDTO;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class OTPService extends BaseService {

    @Value("${base_end_point}")
    private String baseEndPoint;

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;
    private final ProducerTemplate producerTemplate;
    private final OnboardingRepository onboardingRepository;

    public OTPService(SecurityUtils securityUtils, RestTemplate restTemplate,ProducerTemplate producerTemplate,
                      OnboardingRepository onboardingRepository) {
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        this.producerTemplate = producerTemplate;
        this.onboardingRepository = onboardingRepository;
    }

    public RestResponse OTPSend(OTPSendRequestDTO otpSendDTO, String source) {

        try{

            // save activity history
            String timestamp = DateTimeUtil.dateTime();
            LoggingEntity loggingEntity = new LoggingEntity();
            loggingEntity.setIdentifier(otpSendDTO.getIdNumber());

            String encryptedPayload = securityUtils.encrypt(otpSendDTO.toString());

            String results;
            if(source.equals("itrust")){
                results = producerTemplate.requestBody("direct:otp-send",otpSendDTO,String.class);
            }else {
                results = restTemplate.postForObject(baseEndPoint + "/otp/send", otpSendDTO, String.class);
            }
            if(results != null && !results.isEmpty()) {


                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();
                if(Objects.equals(responseDTO.getStatusCode(), "600")) {
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    return successResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, objectNode);
                }else {
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages,null);
                }
            }else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

    public RestResponse OTPVerify(OTPVerifyRequestDTO otpVerifyDTO, String source) {

        try{

            String encryptedPayload = securityUtils.encrypt(otpVerifyDTO.toString());
            String results;
            if(source.equals("itrust")){
               results = producerTemplate.requestBody("direct:otp-verify",otpVerifyDTO,String.class);
            }else {
               results = restTemplate.postForObject(baseEndPoint + "/otp/verify", otpVerifyDTO, String.class);
            }

            if(results != null && !results.isEmpty()) {
                String response = securityUtils.decrypt(results);
                ObjectMapper mapper = new ObjectMapper();
                NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);
                Map<String, Object> messages = new HashMap<>();
                if(Objects.equals(responseDTO.getStatusCode(), "600")) {
                    ObjectNode objectNode = mapper.valueToTree(responseDTO);
                    objectNode.remove("statusCode");
                    AccountEntity saveAccount;
                    saveAccount = onboardingRepository.findFirstByNin(otpVerifyDTO.getIdNumber());
                    if(saveAccount == null) {
                         saveAccount = new AccountEntity();
                    }
                    saveAccount.setNin(otpVerifyDTO.getIdNumber());
                    if(source.equals("itrust")) {
                        saveAccount.setCopReference(objectNode.get("data").get("copReference").asText());
                    }else{
                        saveAccount.setCopReference(objectNode.get("data").get("copreference").asText());
                    }
                    saveAccount.setStatus("pending");
                    onboardingRepository.save(saveAccount);
                    return successResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, objectNode);
                }else {
                    return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages,null);
                }
            }else {
                Map<String, Object> messages = new HashMap<>();
                                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages,null);
            }
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

}
