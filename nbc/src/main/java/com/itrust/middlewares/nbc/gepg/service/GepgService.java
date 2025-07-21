package com.itrust.middlewares.nbc.gepg.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.JsonUtils;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.gepg.requests.*;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * Service class for handling Government Electronic Payment Gateway (GePG) operations.
 * This service provides functionality for bill inquiry, payment confirmation, and payment processing.
 * It extends the BaseService to leverage common logging and response handling capabilities.
 * 
 * @author iTrust
 * @version 1.0
 */
@Service
public class GepgService extends BaseService {

    /** Base endpoint URL for the iTrust service */
    @Value("${itrust-service}")
    private String baseEndPoint;

    /** RestTemplate for making HTTP requests */
    private final RestTemplate restTemplate;

    /** Utility for encryption and decryption operations */
    private final SecurityUtils securityUtils;

    @Value("${gepg.callback.url}")
    private String callbackUrl;

    /**
     * Constructor for GepgService.
     *
     * @param restTemplate RestTemplate for making HTTP requests
     * @param securityUtils Utility for encryption and decryption operations
     */
    public GepgService(RestTemplate restTemplate, SecurityUtils securityUtils) {
        
        this.restTemplate = restTemplate;
        this.securityUtils = securityUtils;
        logInfo("GepgService initialized");
    }

    /**
     * Processes a GePG bill inquiry request.
     * <p>
     * This method handles the inquiry process for GePG bills, including:
     * 1. Logging the request details
     * 2. Sending the request to the backend service
     * 3. Processing and transforming the response
     * 4. Handling success and error scenarios
     *
     * @param requestDTO The inquiry request data transfer object containing customer account and other details
     * @return A GenericRestResponse containing the inquiry result or error information
     */
    @Transactional
    public GenericRestResponse<?> inquiry(InquiryDTO requestDTO) {
        logInfo("Processing GePG inquiry request for account: " + requestDTO.getCustomerAccount());

        try {
            // Encrypt payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Payload encrypted successfully");

            // Make the actual request to the backend service
            logInfo("Sending inquiry request to: " + baseEndPoint + "/i-trust/gepg/inquiry/card-less");
            String results = restTemplate.postForObject(baseEndPoint+"/i-trust/gepg/inquiry/card-less", requestDTO, String.class);
            logDebug("Received response from backend service");

            // Log the raw response for debugging purposes
            logInfo("GePG inquiry response received: " + results);

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty response");


                // Parse the JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);
                logDebug("Response JSON parsed successfully");

                // Convert field names to camelCase for consistency
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);
                logDebug("Response converted to camelCase format");

                // Extract status code and message from the response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Response status: code=" + responseCode + ", message=" + responseMessage);

                // Remove status code and message from the data object
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Check if the response indicates success (code 7101)
                if(Objects.equals(responseCode, "7101")) {
                    logInfo("GePG inquiry successful for account: " + requestDTO.getCustomerAccount());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), messages, objectNode);
                } else {
                    logWarn("GePG inquiry failed with code: " + responseCode + " for account: " + requestDTO.getCustomerAccount());
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                // Handle an empty response scenario
                logWarn("Empty response received from GePG service for account: " + requestDTO.getCustomerAccount());
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                    messages,
                    null
                );
            }
        } catch (Exception e) {
            // Log the exception details
            logError("Exception occurred during GePG inquiry for account: " + requestDTO.getCustomerAccount() + " - " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Processes a GePG payment confirmation request.
     * <p>
     * This method handles the confirmation process for GePG payments, including
     * 1. Logging the request details
     * 2. Sending the confirmation request to the backend service
     * 3. Processing and transforming response
     * 4. Handling success and error scenarios
     *
     * @param requestDTO The confirmation request data transfer object containing channel reference and other details
     * @return A GenericRestResponse containing the confirmation result or error information
     */
    @Transactional
    public GenericRestResponse<?> confirm(ConfirmDTO requestDTO) {
        logInfo("Processing GePG confirmation request for channel reference: " + requestDTO.getChannelRef());

        try {
            // Encrypt payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Payload encrypted successfully");

            // Make the actual request to the backend service
            logInfo("Sending confirmation request to: " + baseEndPoint + "/i-trust/gepg/confirm/card-less");
            String results = restTemplate.postForObject(baseEndPoint+"/i-trust/gepg/confirm/card-less", requestDTO, String.class);
            logDebug("Received response from backend service");

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty response");

                // Decrypt the response if it's encrypted
                String response = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                // Parse the JSON response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);
                logDebug("Response JSON parsed successfully");

                // Convert field names to camelCase for consistency
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);
                logDebug("Response converted to camelCase format");

                // Extract status code and message from the response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Response status: code=" + responseCode + ", message=" + responseMessage);

                // Remove status code and message from the data object
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Check if the response indicates success (code 600)
                if(Objects.equals(responseCode, "600")) {
                    logInfo("GePG confirmation successful for channel reference: " + requestDTO.getChannelRef());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), null, camelCaseNode);
                } else {
                    logWarn("GePG confirmation failed with code: " + responseCode + " for channel reference: " + requestDTO.getChannelRef());
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                // Handle an empty response scenario
                logWarn("Empty response received from GePG service for channel reference: " + requestDTO.getChannelRef());
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                    messages,
                    null
                );
            }

        } catch (Exception e) {
            // Log the exception details
            logError("Exception occurred during GePG confirmation for channel reference: " + requestDTO.getChannelRef() + " - " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Processes a GePG payment request.
     * <p>
     * This method handles the payment process for GePG, including
     * 1. Logging the request details
     * 2. Sending the payment request to the backend service
     * 3. Processing and transforming response
     * 4. Handling success and error scenarios
     *
     * @param requestDTO The payment request data transfer object containing channel reference and payment details
     * @return A GenericRestResponse containing the payment result or error information
     */
    @Transactional
    public GenericRestResponse<?> payment(PaymentDTO requestDTO) {
        logInfo("Processing GePG payment request for channel reference: " + requestDTO.getChannelRef());

        try {

            requestDTO.setCallbackUrl(callbackUrl);

            // Initialize object mapper for JSON processing
            ObjectMapper mapper = new ObjectMapper();

            // Encrypt payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Payload encrypted successfully");

            // Make the actual request to the backend service
            logger.info("Sending payment request to: {}, payload; {}" ,baseEndPoint + "/i-trust/gepg/payment/card-less",requestDTO);
            String results = restTemplate.postForObject(baseEndPoint+"/i-trust/gepg/payment/card-less", requestDTO, String.class);
            logDebug("Received response from backend service");

            // Log the raw response for debugging purposes
            logInfo("GePG payment response received: " + results);

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty response");

                // Decrypt the response if it's encrypted
                String response = securityUtils.decrypt(results);

                // Parse the JSON response
                JsonNode rootNode = mapper.readTree(results);
                logDebug("Response JSON parsed successfully");

                // Convert field names to camelCase for consistency
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);
                logDebug("Response converted to camelCase format");

                // Extract status code and message from the response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Response status: code=" + responseCode + ", message=" + responseMessage);

                // Remove status code and message from the data object
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Check if the response indicates success (code 7379)
                if(Objects.equals(responseCode, "7379")) {
                    logInfo("GePG payment successful for channel reference: " + requestDTO.getChannelRef());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), null, objectNode);
                } else {
                    logWarn("GePG payment failed with code: " + responseCode + " for channel reference: " + requestDTO.getChannelRef());
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                // Handle an empty response scenario
                logWarn("Empty response received from GePG service for channel reference: " + requestDTO.getChannelRef());
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                    messages,
                    null
                );
            }
        } catch (Exception e) {
            // Log the exception details
            logError("Exception occurred during GePG payment for channel reference: " + requestDTO.getChannelRef() + " - " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

}
