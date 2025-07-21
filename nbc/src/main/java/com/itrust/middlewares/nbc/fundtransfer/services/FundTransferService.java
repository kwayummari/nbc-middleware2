package com.itrust.middlewares.nbc.fundtransfer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.fundtransfer.dtos.requests.FundTransferRequestDTO;
import com.itrust.middlewares.nbc.logging.data.LoggingType;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import com.itrust.middlewares.nbc.responses.NBCResponseDTO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Service responsible for handling fund transfer operations.
 * 
 * This service provides functionality for stock and unit buying/selling operations
 * by communicating with external fund transfer endpoints. It handles the creation,
 * logging, and processing of fund transfer requests for different financial instruments.
 * 
 * The service extends BaseService to leverage common functionality like logging and response handling.
 */
@Service
public class FundTransferService extends BaseService {

    /** Base endpoint for fund transfer operations, configured in application properties */
    @Value("${fund_transfer_end_point}")
    private String baseEndPoint;

    /** Callback URL for stock operations, configured in application properties */
    @Value("${stocks_callback_url}")
    private String stocksCallbackUrl;

    /** Callback URL for unit operations, configured in application properties */
    @Value("${units_callback_url}")
    private String unitsCallbackUrl;

    /** Repository for storing historical transaction data */
    private final LoggingRepository historyRepository;

    /** Template for making REST calls to external services */
    private final RestTemplate restTemplate;

    /**
     * Constructs a new FundTransferService with required dependencies.
     *
     * @param restTemplate Template for making REST calls
     * @param historyRepository Repository for storing historical transaction data
     */
    public FundTransferService(RestTemplate restTemplate,LoggingRepository historyRepository) {
        this.restTemplate = restTemplate;
        this.historyRepository = historyRepository;
        logInfo("FundTransferService initialized");
    }

    /**
     * Processes a stock sell request.
     * This method handles the selling of stocks by creating and sending a fund transfer request.
     *
     * @param fundTransferRequestDTO The fund transfer request data for selling stocks
     * @return A RestResponse containing the result of the stock sell operation
     */
    public RestResponse stockSell(FundTransferRequestDTO fundTransferRequestDTO) {
        logInfo("Processing stock sell request: " + fundTransferRequestDTO);
        return getRestResponse(fundTransferRequestDTO);
    }

    /**
     * Helper method to process a fund transfer request.
     * This method prepares the request data, logs the request, and sends it to the external service.
     *
     * @param fundTransferRequestDTO The fund transfer request data
     * @return A RestResponse containing the result of the fund transfer operation
     */
    private RestResponse getRestResponse(FundTransferRequestDTO fundTransferRequestDTO) {
        logInfo("Preparing fund transfer request data");
        try {
            // Create timestamp and logging entity
            String timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
            LoggingEntity loggingEntity = new LoggingEntity();

            // Prepare request data for logging
            HashMap<String, String> requestData = new HashMap<>();
            requestData.put("type", LoggingType.login.toString());
            requestData.put("timestamp", timestamp);
            requestData.put("payload", fundTransferRequestDTO.toString());
            requestData.put("method", "POST");
            requestData.put("controllerAction", "StocksController/stocks/buy");

            logInfo("Sending fund transfer request to external service");
            return getRestResponse(fundTransferRequestDTO, loggingEntity, requestData, stocksCallbackUrl);
        } catch (Exception e) {
            logError("Exception occurred during fund transfer request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Processes a stock buy request.
     * This method handles the buying of stocks by creating and sending a fund transfer request.
     *
     * @param fundTransferRequestDTO The fund transfer request data for buying stocks
     * @return A RestResponse containing the result of the stock buy operation
     */
    public RestResponse stockBuy(FundTransferRequestDTO fundTransferRequestDTO) {
        logInfo("Processing stock buy request: " + fundTransferRequestDTO);
        return getRestResponse(fundTransferRequestDTO);
    }

    /**
     * Processes a unit sell request.
     * This method handles the selling of units by creating and sending a fund transfer request.
     *
     * @param fundTransferRequestDTO The fund transfer request data for selling units
     * @return A RestResponse containing the result of the unit sell operation
     */
    public RestResponse unitSell(FundTransferRequestDTO fundTransferRequestDTO) {
        logInfo("Processing unit sell request: " + fundTransferRequestDTO);

        try {
            // Set the callback URL for the unit sell operation
            fundTransferRequestDTO.setCallbackUrl(unitsCallbackUrl);
            logInfo("Set callback URL to: " + unitsCallbackUrl);

            // Send the request to the external service
            logInfo("Sending unit sell request to: " + baseEndPoint);
            String results = restTemplate.postForObject(baseEndPoint, fundTransferRequestDTO, String.class);
            logInfo("Received response from unit sell endpoint");

            return getRestResponse(results);
        } catch (Exception e) {
            logError("Exception occurred during unit sell request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Helper method to process a response string from the external service.
     * This method parses the response, extracts status information, and creates an appropriate RestResponse.
     *
     * @param results The response string from the external service
     * @return A RestResponse containing the processed result
     * @throws com.fasterxml.jackson.core.JsonProcessingException If there's an error parsing the JSON response
     */
    private RestResponse getRestResponse(String results) throws com.fasterxml.jackson.core.JsonProcessingException {
        logInfo("Processing response from external service");
        ObjectMapper mapper = new ObjectMapper();

        if(results != null && !results.isEmpty()) {
            logDebug("Response is not empty, parsing JSON");

            // Parse the response into a DTO
            NBCResponseDTO responseDTO = mapper.readValue(results, NBCResponseDTO.class);

            // Extract status and message from response
            String responseCode = responseDTO.getStatusCode();
            String responseMessage = responseDTO.getMessage();
            logInfo("Response received with code: " + responseCode + ", message: " + responseMessage);

            // Prepare response data
            Map<String, Object> messages = new HashMap<>();
            ObjectNode objectNode = mapper.valueToTree(responseDTO);
            objectNode.remove("statusCode");

            // Return appropriate response based on status code
            if(Objects.equals(responseCode, "600")) {
                logInfo("Operation successful with code: " + responseCode);
                return successResponse(responseCode, responseMessage, null, objectNode);
            } else {
                logWarn("Operation failed with code: " + responseCode);

                // Process error details if available
                if(responseDTO.getErrors() != null) {
                    logDebug("Processing error details from response");
                    for (Map<String, Object> error : responseDTO.getErrors()) {
                        messages.put((String) error.get("field"), error.get("message"));
                        logWarn("Error in field '" + error.get("field") + "': " + error.get("message"));
                    }
                }

                return errorResponse(responseDTO.getMessage(), responseDTO.getStatusCode(), messages, null);
            }
        } else {
            logError("Empty response received from external service");
            Map<String, Object> messages = new HashMap<>();
            return errorResponse(ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getMessage(),
                               ResponseCode.EMPTY_RESPONSE_FROM_THIRD_PARTY.getCode(), 
                               messages, null);
        }
    }

    /**
     * Processes a unit buy request.
     * This method handles the buying of units by creating and sending a fund transfer request.
     * It also logs the request and response details for auditing purposes.
     *
     * @param fundTransferRequestDTO The fund transfer request data for buying units
     * @return A RestResponse containing the result of the unit buy operation
     */
    public RestResponse unitBuy(FundTransferRequestDTO fundTransferRequestDTO) {
        logInfo("Processing unit buy request: " + fundTransferRequestDTO);

        // Create a logging entity for this transaction
        LoggingEntity loggingEntity = new LoggingEntity();
        logDebug("Created logging entity for unit buy transaction");

        try {
            // Create timestamp and prepare request data for logging
            String timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
            HashMap<String, String> requestData = new HashMap<>();
            requestData.put("type", LoggingType.login.toString());
            requestData.put("timestamp", timestamp);
            requestData.put("payload", fundTransferRequestDTO.toString());
            requestData.put("method", "POST");
            requestData.put("controllerAction", "UnitsController/units/buy");

            logInfo("Sending unit buy request to external service with callback URL: " + unitsCallbackUrl);
            return getRestResponse(fundTransferRequestDTO, loggingEntity, requestData, unitsCallbackUrl);
        } catch (Exception e) {
            logError("Exception occurred during unit buy request: " + e.getMessage());

            // Log the exception details to the logging entity
            loggingEntity.setResponse(e.getMessage());
            loggingEntity.setStatus(500);
            historyRepository.save(loggingEntity);
            logInfo("Saved exception details to history repository");

            return exceptionResponse(e, null);
        }
    }

    /**
     * Helper method to process a fund transfer request with detailed logging.
     * This method sets up the request, sends it to the external service, logs the response,
     * and processes the results.
     *
     * @param fundTransferRequestDTO The fund transfer request data
     * @param loggingEntity The logging entity to store request/response details
     * @param requestData Map containing request metadata for logging
     * @param callbackUrl The callback URL to be set in the request
     * @return A RestResponse containing the processed result
     * @throws com.fasterxml.jackson.core.JsonProcessingException If there's an error parsing the JSON response
     */
    private RestResponse getRestResponse(FundTransferRequestDTO fundTransferRequestDTO, LoggingEntity loggingEntity, 
                                        HashMap<String, String> requestData, String callbackUrl) 
                                        throws com.fasterxml.jackson.core.JsonProcessingException {

        // Set the callback URL for the operation
        fundTransferRequestDTO.setCallbackUrl(callbackUrl);
        logInfo("Set callback URL to: " + callbackUrl);

        // Send the request to the external service
        logInfo("Sending request to: " + baseEndPoint);
        String results = restTemplate.postForObject(baseEndPoint, fundTransferRequestDTO, String.class);
        logDebug("Received response from external service");

        // Log the response details
        loggingEntity.setResponse(results);
        loggingEntity.setStatus(200);
        logDebug("Saving response to history repository");
        historyRepository.save(loggingEntity);

        // Process the response
        return getRestResponse(results);
    }

}
