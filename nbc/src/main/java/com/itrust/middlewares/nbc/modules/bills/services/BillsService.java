package com.itrust.middlewares.nbc.modules.bills.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillItem;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsConfirmRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsEnquiryRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsPaymentRequestDTO;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class for handling bill-related operations.
 * This service provides functionality for bill inquiries, confirmations, payments, and retrieving biller information.
 * It communicates with external bill payment services through REST APIs.
 */
@Service("billersService")
public class BillsService extends BaseService {

    @Value("${bills_end_point}")
    private String baseEndPoint;

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;

    /**
     * Constructs a new BillsService with the required dependencies.
     *
     * 
     * @param securityUtils Utility for encryption and decryption operations
     * @param restTemplate Template for making REST API calls
     */
    public BillsService(SecurityUtils securityUtils, RestTemplate restTemplate) {
        
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        logInfo("BillsService initialized with endpoint: " + baseEndPoint);
    }

    /**
     * Performs a bill inquiry operation.
     * Note: the inquiry is invalid after 15 minutes.
     *
     * @param lookupRequestDTO The request data containing bill inquiry details
     * @return A RestResponse containing the result of the inquiry operation
     */
    public RestResponse inquiry(BillsEnquiryRequestDTO lookupRequestDTO) {
        logInfo("Starting bill inquiry for account: " + lookupRequestDTO.getCustomerAccount());

        try {
            String encryptedPayload = securityUtils.encrypt(lookupRequestDTO.toString());
            logDebug("Sending inquiry request to: " + baseEndPoint + "/inquiry/card-less");

            String results = restTemplate.postForObject(baseEndPoint+"/inquiry/card-less", lookupRequestDTO, String.class);

            if (results != null) {
                logInfo("Received response from bill inquiry service");
                logDebug("Bill inquiry response: " + results);
            } else {
                logWarn("Received null response from bill inquiry service");
            }

            RestResponse response = getRestResponse(results);
            logInfo("Bill inquiry completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;

        } catch (Exception e) {
            logError("Error during bill inquiry: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Processes the response from the bill service and converts it to a RestResponse.
     *
     * @param results The raw response string from the bill service
     * @return A RestResponse object containing the processed response
     * @throws com.fasterxml.jackson.core.JsonProcessingException If there's an error processing the JSON response
     */
    private RestResponse getRestResponse(String results) throws com.fasterxml.jackson.core.JsonProcessingException {
        logDebug("Processing response from bill service");

        if(results != null && !results.isEmpty()) {
            String decryptedResponse = securityUtils.decrypt(results);
            logDebug("Response decrypted successfully");

            ObjectMapper mapper = new ObjectMapper();
            NBCResponseDTO restResponse = mapper.readValue(results, NBCResponseDTO.class);
            Map<String, Object> messages = new HashMap<>();

            logInfo("Response status code: " + restResponse.getStatusCode());

            if(Objects.equals(restResponse.getStatusCode(), "600")) {
                logInfo("Processing successful response with message: " + restResponse.getMessage());

                ObjectNode objectNode = mapper.valueToTree(restResponse);
                objectNode.remove("statusCode");

                return successResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages, objectNode);
            } else {
                logWarn("Processing error response with code: " + restResponse.getStatusCode() + ", message: " + restResponse.getMessage());
                return errorResponse(restResponse.getMessage(), restResponse.getStatusCode(), messages, null);
            }
        } else {
            logError("Empty or null response received from bill service");
            Map<String, Object> messages = new HashMap<>();
            return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(), 
                                ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                messages, null);
        }
    }

    /**
     * Confirms a bill payment operation.
     *
     * @param confirmationRequestDTO The request data containing bill confirmation details
     * @return A RestResponse containing the result of the confirmation operation
     */
    public RestResponse confirm(BillsConfirmRequestDTO confirmationRequestDTO) {
        logInfo("Starting bill confirmation for reference: " + confirmationRequestDTO.getChannelRef());

        try {
            String encryptedPayload = securityUtils.encrypt(confirmationRequestDTO.toString());
            logDebug("Sending confirmation request to: " + baseEndPoint + "/confirm/card-less");

            String results = restTemplate.postForObject(baseEndPoint+"/confirm/card-less", confirmationRequestDTO, String.class);

            if (results != null) {
                logInfo("Received response from bill confirmation service");
                logDebug("Bill confirmation response: " + results);
            } else {
                logWarn("Received null response from bill confirmation service");
            }

            RestResponse response = getRestResponse(results);
            logInfo("Bill confirmation completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;

        } catch (Exception e) {
            logError("Error during bill confirmation: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Processes a bill payment operation.
     *
     * @param transferRequestDTO The request data containing bill payment details
     * @return A RestResponse containing the result of the payment operation
     */
    public RestResponse payment(BillsPaymentRequestDTO transferRequestDTO) {
        logInfo("Starting bill payment for reference: " + transferRequestDTO.getChannelRef());

        try {
            String encryptedPayload = securityUtils.encrypt(transferRequestDTO.toString());
            logDebug("Sending payment request to: " + baseEndPoint + "/payment/card-less");

            String results = restTemplate.postForObject(baseEndPoint+"/payment/card-less", transferRequestDTO, String.class);

            if (results != null) {
                logInfo("Received response from bill payment service");
                logDebug("Bill payment response: " + results);
            } else {
                logWarn("Received null response from bill payment service");
            }

            RestResponse response = getRestResponse(results);
            logInfo("Bill payment completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;

        } catch (Exception e) {
            logError("Error during bill payment: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Retrieves a list of available billers.
     *
     * @return A RestResponse containing the list of available billers
     */
    public RestResponse billers() {
        logInfo("Retrieving list of available billers");

        try {
            String encryptedPayload = securityUtils.encrypt("");
            logDebug("Sending request to: " + baseEndPoint + "/billers");

            String results = restTemplate.getForObject(baseEndPoint+"/billers", String.class);

            logger.info("billers response: {}", results);

            if(results != null && !results.isEmpty()) {
                logInfo("Received response from billers service");
                logDebug("Billers response: " + results);

                String decryptedResponse = securityUtils.decrypt(results);
                logDebug("Response decrypted successfully");

                ObjectMapper mapper = new ObjectMapper();
                List<BillItem> restResponse = mapper.readValue(decryptedResponse, new TypeReference<>() {});

                logInfo("Successfully retrieved " + (restResponse != null ? restResponse.size() : 0) + " billers");
                return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null, restResponse);
            } else {
                logWarn("Received empty or null response from billers service");
                Map<String, Object> messages = new HashMap<>();
                return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                                    ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                                    messages, null);
            }

        } catch (Exception e) {
            logError("Error retrieving billers: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

}
