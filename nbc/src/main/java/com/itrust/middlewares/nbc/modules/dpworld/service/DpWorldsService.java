package com.itrust.middlewares.nbc.modules.dpworld.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.SecurityUtils;
import com.itrust.middlewares.nbc.modules.dpworld.dto.*;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Service class for handling bill-related operations.
 * This service provides functionality for bill inquiries, confirmations, payments, and retrieving biller information.
 * It communicates with external bill payment services through REST APIs.
 */
@Service
public class DpWorldsService extends BaseService {

    @Value("${dpworld_endpoint}")
    private String baseEndPoint;

    private final SecurityUtils securityUtils;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    /**
     * Constructs a new BillsService with the required dependencies.
     *
     *
     * @param securityUtils Utility for encryption and decryption operations
     * @param restTemplate Template for making REST API calls
     */
    public DpWorldsService(SecurityUtils securityUtils, RestTemplate restTemplate) {
        this.securityUtils = securityUtils;
        this.restTemplate = restTemplate;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Performs a bill inquiry operation.
     * Note: the inquiry is invalid after 15 minutes.
     *
     * @param lookupRequestDTO The request data containing bill inquiry details
     * @return A RestResponse containing the result of the inquiry operation
     */
    public GenericRestResponse<?> inquiry(DPOInquiryRequestDto lookupRequestDTO) throws Exception {
        logInfo("Starting dpo world inquiry for reference: " + lookupRequestDTO.getBillRef());

            String results = restTemplate.postForObject(baseEndPoint+"/inquiry/card-less", lookupRequestDTO, String.class);
            // Check if the results are not null before processing
            logInfo("Received response from bill inquiry service for reference: " + results);
            if (results != null) {
                logInfo("Received response from bill inquiry service");
                logDebug("Bill inquiry response: " + results);
            } else {
                logWarn("Received null response from bill inquiry service");
            }

        GenericRestResponse<Map<String,Object>> response = new GenericRestResponse<>();
        DPOlInquiryResponseDto responseDto = mapper.readValue(results, DPOlInquiryResponseDto.class);
        response.setMessage(responseDto.getMessage());
        response.setStatusCode(String.valueOf(responseDto.getStatusCode()));
        Map<String, Object> data = new HashMap<>();
        data.put("channelRef", responseDto.getChannelRef());
        data.put("spCode", responseDto.getSpCode());
        data.put("billRef", responseDto.getBillRef());
        data.put("billDetails", responseDto.getBillDetails());
        data.put("payer", responseDto.getPayer());
        data.put("totalCharges", responseDto.getTotalCharges());
        response.setData(data);
        logInfo("Bill inquiry completed for reference: " + lookupRequestDTO.getBillRef());
        return response;
        }

    /**
     * Confirms a bill payment operation.
     *
     * @param confirmationRequestDTO The request data containing bill confirmation details
     * @return A RestResponse containing the result of the confirmation operation
     */
    public GenericRestResponse<?> confirm(DOPOtpVerificationRequestDto confirmationRequestDTO) throws Exception {

        String results = restTemplate.postForObject(baseEndPoint+"/confirm/card-less", confirmationRequestDTO, String.class);
        logger.info("Received response from bill confirmation service for reference: " + results);
        if (results != null) {
            logInfo("Received response from bill inquiry service");
            logDebug("Bill inquiry response: " + results);
        } else {
            logWarn("Received null response from bill inquiry service");
        }

        GenericRestResponse<Map<String,Object>> response = new GenericRestResponse<>();
        DOPOtpVerificationResponseDto responseDto = mapper.readValue(results, DOPOtpVerificationResponseDto.class);
        response.setMessage(responseDto.getMessage());
        response.setStatusCode(String.valueOf(responseDto.getStatusCode()));
        Map<String, Object> data = new HashMap<>();
        data.put("channelRef", responseDto.getChannelRef());
        data.put("totalCharges", responseDto.getTotalCharges());
        response.setData(data);
        logInfo("Bill confirmation completed for reference: " + confirmationRequestDTO.getChannelRef());
        return response;

    }

    /**
     * Processes a bill payment operation.
     *
     * @param transferRequestDTO The request data containing bill payment details
     * @return A RestResponse containing the result of the payment operation
     */
    public GenericRestResponse<?> payment(DPOPaymentRequestDto transferRequestDTO) throws Exception {
        logInfo("Starting bill payment for reference: " + transferRequestDTO.getChannelRef());
        String results = restTemplate.postForObject(baseEndPoint+"/payment/card-less", transferRequestDTO, String.class);

        logger.info("Received response from bill payment service for reference: " + results);
        if (results != null) {
            logInfo("Received response from bill inquiry service");
            logDebug("Bill inquiry response: " + results);
        } else {
            logWarn("Received null response from bill inquiry service");
        }

        GenericRestResponse<Map<String,Object>> response = new GenericRestResponse<>();
        DPOPaymentResponseDto responseDto =  mapper.readValue(results, DPOPaymentResponseDto.class);
        response.setMessage(responseDto.getMessage());
        response.setStatusCode(String.valueOf(responseDto.getStatusCode()));
        Map<String, Object> data = new HashMap<>();
        data.put("channelRef", responseDto.getChannelRef());
        data.put("totalCharges", responseDto.getTotalCharges());
        response.setData(data);
        logInfo("Bill payment completed for reference: " + transferRequestDTO.getChannelRef());
        return response;
    }

}
