package com.itrust.middlewares.nbc.units.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.fundtransfer.dtos.requests.FundTransferRequestDTO;
import com.itrust.middlewares.nbc.fundtransfer.services.FundTransferService;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling unit-related operations.
 * 
 * This controller provides endpoints for buying and selling units,
 * as well as callback endpoints for processing responses from external systems.
 * All endpoints validate the incoming request data and delegate processing to the FundTransferService.
 */
@RestController
@RequestMapping("/units")
public class UnitsController extends BaseController {

    /** Service responsible for handling fund transfer operations for units */
    private final FundTransferService fundTransferService;

    /**
     * Constructs a new UnitsController with the required dependencies.
     *
     * @param fundTransferService Service for handling fund transfer operations
     */
    public UnitsController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
        logInfo("UnitsController initialized");
    }

    /**
     * Endpoint for buying units.
     * This method processes a request to buy units by delegating to the FundTransferService.
     *
     * @param requestDTO The fund transfer request data for buying units
     * @return A RestResponse containing the result of the unit buy operation
     */
    @PostMapping("/buy")
    public RestResponse unitBuy(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received unit buy request");
        try {
            logInfo("Delegating to FundTransferService.unitBuy");
            RestResponse response = fundTransferService.unitBuy(requestDTO);
            logInfo("Unit buy request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing unit buy request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Endpoint for selling units.
     * This method processes a request to sell units by delegating to the FundTransferService.
     *
     * @param requestDTO The fund transfer request data for selling units
     * @return A RestResponse containing the result of the unit sell operation
     */
    @PostMapping("/sell")
    public RestResponse unitSell(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received unit sell request");
        try {
            logInfo("Delegating to FundTransferService.unitSell");
            RestResponse response = fundTransferService.unitSell(requestDTO);
            logInfo("Unit sell request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing unit sell request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Callback endpoint for unit sell operations.
     * This endpoint receives callbacks from external systems after a unit sell operation.
     * 
     * @param requestDTO The callback data from the external system
     * @return A RestResponse containing the result of processing the callback
     */
    @PostMapping("/sell-callback")
    public RestResponse unitSellCallback(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received unit sell callback");
        try {
            // TODO: Implement callback processing
            logWarn("Unit sell callback implementation pending");
            Map<String, Object> responseMap = new HashMap<>();
            return errorResponse("", "todo", responseMap, new Object());
        } catch (Exception e) {
            logError("Error processing unit sell callback: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Callback endpoint for unit buy operations.
     * This endpoint receives callbacks from external systems after a unit buy operation.
     * 
     * @param requestDTO The callback data from the external system
     * @return A RestResponse containing the result of processing the callback
     */
    @PostMapping("/buy-callback")
    public RestResponse unitBuyCallback(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received unit buy callback");
        try {
            // TODO: Implement callback processing
            logWarn("Unit buy callback implementation pending");
            Map<String, Object> responseMap = new HashMap<>();
            return errorResponse("", "todo", responseMap, new Object());
        } catch (Exception e) {
            logError("Error processing unit buy callback: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }


}
