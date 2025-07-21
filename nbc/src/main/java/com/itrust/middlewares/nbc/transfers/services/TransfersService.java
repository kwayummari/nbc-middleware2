package com.itrust.middlewares.nbc.transfers.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.JsonUtils;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.transfers.dto.TransferConfirmRequestDTO;
import com.itrust.middlewares.nbc.transfers.dto.TransferInquiryRequestDTO;
import com.itrust.middlewares.nbc.transfers.dto.TransferVerifyRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service class responsible for handling transfer-related operations.
 * This service provides functionality for transfer inquiries, verification, and confirmation
 * by communicating with external transfer endpoints.
 * <p>
 * The service extends BaseService to leverage common functionality like logging and response handling.
 */
@Service
public class TransfersService extends BaseService {

    /** Base endpoint for transfer operations, configured in application properties */
    @Value("${transfers_end_point}")
    private String baseEndPoint;

    /** Utility for handling security operations like encryption */
    private final SecurityUtils securityUtils;

    /** Template for making REST calls to external services */
    private final RestTemplate restTemplate;

    @Value("${internal.transfer.callback.url}")
    private String callbackUrl;

    /**
     * Constructs a new TransfersService with required dependencies.
     *
     * @param loggingRepository Repository for storing general logs
     * @param securityUtils Utility for encryption and security operations
     * @param restTemplate Template for making REST calls
     * @param historyRepository Repository for storing historical data
     */
    public TransfersService(SecurityUtils securityUtils, RestTemplate restTemplate, LoggingRepository historyRepository) {
        
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        logInfo("TransfersService initialized");
    }

    /**
     * Processes a transfer inquiry request.
     * This method sends the inquiry to the external transfer service and processes the response.
     *
     * @param inquiryRequestDTO The transfer inquiry request data
     * @return A GenericRestResponse containing the result of the inquiry
     */
    public GenericRestResponse<?> inquiry(TransferInquiryRequestDTO inquiryRequestDTO) {
        logInfo("Processing transfer inquiry request: " + inquiryRequestDTO);

        try {
            // Encrypt the payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(inquiryRequestDTO.toString());
            logDebug("Payload encrypted successfully");

            // Send the request to the external service
            logInfo("Sending inquiry request to: " + baseEndPoint + "/inquiry");
            String results = restTemplate.postForObject(baseEndPoint+"/inquiry", inquiryRequestDTO, String.class);
            logDebug("Received response from inquiry endpoint");

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty response");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Inquiry response received with code: " + responseCode + ", message: " + responseMessage);

                // Remove metadata from the response body
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Return the appropriate response based on status code
                if(Objects.equals(responseCode, "600")) {
                    logInfo("Inquiry successful");
                    return successResponseGeneric(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Inquiry failed with code: " + responseCode);
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }
            } else {
                logError("Empty response received from transfer inquiry endpoint");
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), 
                                           ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                           messages, null);
            }

        } catch (Exception e) {
            logError("Exception occurred during transfer inquiry: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Verifies a transfer request with the external service.
     * This method sends verification data to the external transfer service and processes the response.
     *
     * @param verifyRequestDTO The transfer verification request data
     * @return A GenericRestResponse containing the result of the verification
     */
    public GenericRestResponse<?> verify(TransferVerifyRequestDTO verifyRequestDTO) {
        logInfo("Processing transfer verification request: " + verifyRequestDTO);

        try {
            // Encrypt the payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(verifyRequestDTO.toString());
            logDebug("Verification payload encrypted successfully");

            // Send the request to the external service
            logInfo("Sending verification request to: " + baseEndPoint + "/verify");
            String results = restTemplate.postForObject(baseEndPoint+"/verify", verifyRequestDTO, String.class);
            logDebug("Received response from verification endpoint");

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty verification response");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Verification response received with code: " + responseCode + ", message: " + responseMessage);

                // Remove metadata from the response body
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Return the appropriate response based on status code
                if(Objects.equals(responseCode, "600")) {
                    logInfo("Verification successful");
                    return successResponseGeneric(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Verification failed with code: " + responseCode);
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                logError("Empty response received from transfer verification endpoint");
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                          ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                          messages, null);
            }

        } catch (Exception e) {
            logError("Exception occurred during transfer verification: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Confirms a transfer request with the external service.
     * This method sends confirmation data to the external transfer service and processes the response.
     * This is typically the final step in the transfer process after inquiry and verification.
     *
     * @param confirmRequestDTO The transfer confirmation request data
     * @return A GenericRestResponse containing the result of the confirmation
     */
    public GenericRestResponse<?> confirm(TransferConfirmRequestDTO confirmRequestDTO) {
        logInfo("Processing transfer confirmation request: " + confirmRequestDTO);

        try {
            confirmRequestDTO.setCallbackUrl(callbackUrl);
            // Encrypt the payload for secure transmission
            String encryptedPayload = securityUtils.encrypt(confirmRequestDTO.toString());
            logDebug("Confirmation payload encrypted successfully");

            // Send the request to the external service
            logInfo("Sending confirmation request to: " + baseEndPoint + "/confirm");
            String results = restTemplate.postForObject(baseEndPoint+"/confirm", confirmRequestDTO, String.class);
            logDebug("Received response from confirmation endpoint");

            if(results != null && !results.isEmpty()) {
                logDebug("Processing non-empty confirmation response");

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("Confirmation response received with code: " + responseCode + ", message: " + responseMessage);

                // Remove metadata from the response body
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");

                Map<String, Object> messages = new HashMap<>();

                // Return the appropriate response based on status code
                if(Objects.equals(responseCode, "600")) {
                    logInfo("Transfer confirmation successful");
                    return successResponseGeneric(responseMessage, responseCode, null, objectNode);
                } else {
                    logWarn("Transfer confirmation failed with code: " + responseCode);
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                logError("Empty response received from transfer confirmation endpoint");
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                          ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                          messages, null);
            }

        } catch (Exception e) {
            logError("Exception occurred during transfer confirmation: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

}
