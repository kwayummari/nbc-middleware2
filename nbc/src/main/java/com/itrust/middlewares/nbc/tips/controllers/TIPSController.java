package com.itrust.middlewares.nbc.tips.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.tips.services.TipsService;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSTransferRequestDTO;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSConfirmRequestDTO;
import com.itrust.middlewares.nbc.tips.tdo.requests.TIPSLookupRequestDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling TIPS (Tanzania Instant Payment System) operations.
 * This controller provides endpoints for retrieving financial service providers,
 * looking up recipients, confirming transactions, and transferring funds through TIPS.
 */
@RestController
@RequestMapping("/tips")
@Validated
public class TIPSController extends BaseController {

    private final TipsService tipsService;

    /**
     * Constructs a new TIPSController with the required dependencies.
     *
     * @param tipsService Service for handling TIPS operations
     */
    public TIPSController(TipsService tipsService) {
        this.tipsService = tipsService;
        logInfo("TIPSController initialized");
    }

    /**
     * Retrieves a list of Financial Service Providers (FSPs) from TIPS.
     *
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the list of available FSPs
     */
    @RequestMapping("/fsp")
    public RestResponse options(@RequestHeader("source") String source) {
        logInfo("Received request to retrieve FSPs from source: " + source);
        try {
            RestResponse response = tipsService.fsp(source);
            logInfo("FSP retrieval request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing FSP retrieval request: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    /**
     * Performs a TIPS lookup operation to find recipient information.
     *
     * @param requestDTO The request data containing lookup details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the lookup operation
     */
    @PostMapping("/lookup")
    public RestResponse enquiry(@RequestBody @Valid TIPSLookupRequestDTO requestDTO, @RequestHeader("source") String source) {
        logInfo("Received lookup request for account: " + requestDTO.getCustomerAccount() + " from source: " + source);
        try {
            RestResponse response = tipsService.lookup(requestDTO, source);
            logInfo("Lookup request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing lookup request: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    /**
     * Confirms a TIPS transaction after lookup.
     *
     * @param requestDTO The request data containing confirmation details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the confirmation operation
     */
    @PostMapping("/confirm")
    public RestResponse confirm(@RequestBody @Valid TIPSConfirmRequestDTO requestDTO, @RequestHeader("source") String source) {
        logInfo("Received confirmation request for channel reference: " + requestDTO.getChannelRef() + " from source: " + source);
        try {
            RestResponse response = tipsService.confirm(requestDTO, source);
            logInfo("Confirmation request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing confirmation request: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    /**
     * Initiates a funds transfer through TIPS.
     *
     * @param requestDTO The request data containing transfer details
     * @param source The source of the request (e.g., "itrust" or other)
     * @return A RestResponse containing the result of the transfer operation
     */
    @PostMapping("/transfer")
    public RestResponse transfer(@RequestBody @Valid TIPSTransferRequestDTO requestDTO, @RequestHeader("source") String source) {
        logInfo("Received transfer request for channel reference: " + requestDTO.getChannelRef() + " from source: " + source);
        try {
            RestResponse response = tipsService.transfer(requestDTO, source);
            logInfo("Transfer request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing transfer request: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

}
