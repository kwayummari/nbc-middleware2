package com.itrust.middlewares.nbc.stocks.controllers;

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
 * REST controller for handling stock-related operations.
 * 
 * This controller provides endpoints for buying and selling stocks,
 * as well as callback endpoints for processing responses from external systems.
 * All endpoints validate the incoming request data and delegate processing to the FundTransferService.
 */
@RestController
@RequestMapping("/stocks")
public class StocksController extends BaseController {

    /** Service responsible for handling fund transfer operations for stocks */
    private final FundTransferService fundTransferService;

    /**
     * Constructs a new StocksController with the required dependencies.
     *
     * @param fundTransferService Service for handling fund transfer operations
     */
    public StocksController(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
        logInfo("StocksController initialized");
    }

    /**
     * Endpoint for buying stocks.
     * This method processes a request to buy stocks by delegating to the FundTransferService.
     *
     * @param requestDTO The fund transfer request data for buying stocks
     * @return A RestResponse containing the result of the stock buy operation
     */
    @PostMapping("/buy")
    public RestResponse stockBuy(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received stock buy request");
        try {
            logInfo("Delegating to FundTransferService.stockBuy");
            RestResponse response = fundTransferService.stockBuy(requestDTO);
            logInfo("Stock buy request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing stock buy request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Endpoint for selling stocks.
     * This method processes a request to sell stocks by delegating to the FundTransferService.
     *
     * @param requestDTO The fund transfer request data for selling stocks
     * @return A RestResponse containing the result of the stock sell operation
     */
    @PostMapping("/sell")
    public RestResponse stockSell(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received stock sell request");
        try {
            logInfo("Delegating to FundTransferService.stockSell");
            RestResponse response = fundTransferService.stockSell(requestDTO);
            logInfo("Stock sell request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing stock sell request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Callback endpoint for stock sell operations.
     * This endpoint receives callbacks from external systems after a stock sell operation.
     * 
     * @param requestDTO The callback data from the external system
     * @return A RestResponse containing the result of processing the callback
     */
    @PostMapping("/sell-callback")
    public RestResponse stockSellCallback(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received stock sell callback");
        try {
            // TODO: Implement callback processing
            logWarn("Stock sell callback implementation pending");
            Map<String, Object> responseMap = new HashMap<>();
            return errorResponse("", "todo", responseMap, new Object());
        } catch (Exception e) {
            logError("Error processing stock sell callback: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Callback endpoint for stock buy operations.
     * This endpoint receives callbacks from external systems after a stock buy operation.
     * 
     * @param requestDTO The callback data from the external system
     * @return A RestResponse containing the result of processing the callback
     */
    @PostMapping("/buy-callback")
    public RestResponse stockBuyCallback(@RequestBody @Valid FundTransferRequestDTO requestDTO) {
        logInfo("Received stock buy callback");
        try {
            // TODO: Implement callback processing
            logWarn("Stock buy callback implementation pending");
            Map<String, Object> responseMap = new HashMap<>();
            return errorResponse("", "todo", responseMap, new Object());
        } catch (Exception e) {
            logError("Error processing stock buy callback: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

}
