package com.itrust.middlewares.nbc.luku.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.JsonUtils;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.luku.requests.ConfirmDTO;
import com.itrust.middlewares.nbc.luku.requests.InquiryDTO;
import com.itrust.middlewares.nbc.luku.requests.PaymentDTO;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * Service class for handling LUKU (prepaid electricity) operations.
 * This service provides functionality for meter inquiry, payment confirmation,
 * and processing payments for LUKU services. It communicates with the NBC backend
 * system to perform these operations.
 * 
 * @author iTrust Team
 * @version 1.0
 */
@Service
public class LukuService extends BaseService {

    /** Base endpoint URL for the iTrust service */
    @Value("${itrust-service}")
    private String baseEndPoint;

    /** Callback URL for LUKU payment processing */
    @Value("${luku_callback_url}")
    private String lukuCallbackUrl;

    /** RestTemplate for making HTTP requests */
    private final RestTemplate restTemplate;

    /** Utility for encryption/decryption operations */
    private final SecurityUtils securityUtils;

    /**
     * Constructor for LukuService.
     * 
     * 
     * @param restTemplate Template for making REST API calls
     * @param securityUtils Utility for handling security operations like encryption/decryption
     */
    public LukuService(RestTemplate restTemplate, SecurityUtils securityUtils) {
        
        this.restTemplate = restTemplate;
        this.securityUtils = securityUtils;
    }

    /**
     * Performs a LUKU meter inquiry operation.
     * This method sends a request to the NBC backend to retrieve information about a LUKU meter.
     * 
     * @param requestDTO The inquiry request data containing meter number and customer account
     * @return GenericRestResponse containing the inquiry result or error information
     */
    @Transactional
    public GenericRestResponse<?> inquiry(InquiryDTO requestDTO) {
        logInfo("Starting LUKU inquiry for meter: " + requestDTO.getMeter() + ", account: " + requestDTO.getCustomerAccount());

        try {

            // Encrypt payload and send request to backend
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Sending LUKU inquiry request to: " + baseEndPoint + "/i-trust/luku/inquiry/card-less");
            String results = restTemplate.postForObject(baseEndPoint + "/i-trust/luku/inquiry/card-less", requestDTO, String.class);

            // Process response
            if (results != null) {
                logDebug("Received response from LUKU inquiry endpoint");
                String response = securityUtils.decrypt(results);


                // Parse and process the response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("LUKU inquiry response received with code: " + responseCode + ", message: " + responseMessage);

                // Prepare response data
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");
                Map<String, Object> messages = new HashMap<>();

                // Return appropriate response based on status code
                if (Objects.equals(responseCode, "7101")) {
                    logInfo("LUKU inquiry successful for meter: " + requestDTO.getMeter());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), null, objectNode);
                } else {
                    logWarn("LUKU inquiry failed with code: " + responseCode + ", message: " + responseMessage);
                    ObjectNode objectNodeError = new ObjectMapper().createObjectNode();
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }
            } else {
                logError("Received null response from LUKU inquiry endpoint");
                return errorResponseGeneric("No response received from service", 
                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), new HashMap<>(), null);
            }
        } catch (Exception e) {
            logError("Exception occurred during LUKU inquiry: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Confirms a LUKU payment transaction.
     * This method sends a confirmation request to the NBC backend to finalize a LUKU payment.
     * 
     * @param requestDTO The confirmation request data containing channel reference and amount
     * @return GenericRestResponse containing the confirmation result or error information
     */
    @Transactional
    public GenericRestResponse<?> confirm(ConfirmDTO requestDTO) {
        logInfo("Starting LUKU payment confirmation for channel reference: " + requestDTO.getChannelRef() + 
                ", amount: " + requestDTO.getAmount());

        try {

            // Encrypt payload and send request to backend
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Sending LUKU confirmation request to: " + baseEndPoint + "/i-trust/luku/confirm/card-less");
            String results = restTemplate.postForObject(baseEndPoint + "/i-trust/luku/confirm/card-less", requestDTO, String.class);

            if (results != null && !results.isEmpty()) {
                logDebug("Received response from LUKU confirmation endpoint");
                String response = securityUtils.decrypt(results);

                // Parse and process the response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("LUKU confirmation response received with code: " + responseCode + ", message: " + responseMessage);

                // Prepare response data
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");
                Map<String, Object> messages = new HashMap<>();

                // Return appropriate response based on status code
                if (Objects.equals(responseCode, "600")) {
                    logInfo("LUKU confirmation successful for channel reference: " + requestDTO.getChannelRef());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), messages, objectNode);
                } else {
                    logWarn("LUKU confirmation failed with code: " + responseCode + ", message: " + responseMessage);
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                logError("Received empty or null response from LUKU confirmation endpoint");
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                        ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages, null);
            }

        } catch (Exception e) {
            logError("Exception occurred during LUKU confirmation: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Processes a LUKU payment transaction.
     * This method sends a payment request to the NBC backend to purchase LUKU units.
     * 
     * @param requestDTO The payment request data containing customer and transaction details
     * @return GenericRestResponse containing the payment result or error information
     */
    @Transactional
    public GenericRestResponse<?> payment(PaymentDTO requestDTO) {
        logInfo("Starting LUKU payment for channel reference: " + requestDTO.getChannelRef() + 
                ", customer: " + requestDTO.getCustomerName());

        try {

            // Set callback URL for payment notifications
            requestDTO.setCallbackUrl(lukuCallbackUrl);
            logDebug("Set callback URL to: " + lukuCallbackUrl);

            // Encrypt payload and send request to backend
            String encryptedPayload = securityUtils.encrypt(requestDTO.toString());
            logDebug("Sending LUKU payment request to: " + baseEndPoint + "/i-trust/luku/payment/card-less");
            String results = restTemplate.postForObject(baseEndPoint + "/i-trust/luku/payment/card-less", requestDTO, String.class);

            if (results != null && !results.isEmpty()) {
                logDebug("Received response from LUKU payment endpoint");
                String response = securityUtils.decrypt(results);


                // Parse and process the response
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(results);

                // Convert to camelCase for consistent property naming
                JsonNode camelCaseNode = JsonUtils.convertToCamelCase(rootNode, mapper);

                // Extract status and message from response
                String responseCode = String.valueOf(camelCaseNode.get("statusCode"));
                String responseMessage = camelCaseNode.get("message").asText();
                logInfo("LUKU payment response received with code: " + responseCode + ", message: " + responseMessage);

                // Prepare response data
                ObjectNode objectNode = mapper.convertValue(camelCaseNode, ObjectNode.class);
                objectNode.remove("statusCode");
                objectNode.remove("message");
                Map<String, Object> messages = new HashMap<>();

                // Return appropriate response based on status code
                if (Objects.equals(responseCode, "7379")) {
                    logInfo("LUKU payment successful for channel reference: " + requestDTO.getChannelRef());
                    return successResponseGeneric(responseMessage, ResponseCode.SUCCESS.getCode(), null, objectNode);
                } else {
                    logWarn("LUKU payment failed with code: " + responseCode + ", message: " + responseMessage);
                    return errorResponseGeneric(responseMessage, responseCode, messages, null);
                }

            } else {
                Map<String, Object> messages = new HashMap<>();
                return errorResponseGeneric(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                        ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), messages, null);
            }
        } catch (Exception e) {
            logError("Exception occurred during LUKU payment: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

}
