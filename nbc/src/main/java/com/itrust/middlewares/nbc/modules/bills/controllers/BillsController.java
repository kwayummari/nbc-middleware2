package com.itrust.middlewares.nbc.modules.bills.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.modules.bills.services.BillsService;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsConfirmRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsEnquiryRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsPaymentRequestDTO;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling bill-related HTTP requests.
 * This controller provides endpoints for retrieving billers, bill inquiries, confirmations, and payments.
 * It delegates the business logic to the BillsService.
 */
@RestController
@RequestMapping("/bills")
@Validated
public class BillsController extends BaseController {

    private final BillsService billsService;

    /**
     * Constructs a new BillsController with the required dependencies.
     *
     * @param billsService Service for handling bill-related operations
     */
    public BillsController(BillsService billsService) {
        this.billsService = billsService;
        logInfo("BillsController initialized");
    }

    /**
     * Retrieves a list of available billers.
     *
     * @return A RestResponse containing the list of available billers
     */
    @RequestMapping("/billers")
    public RestResponse options() {
        logInfo("Received request to get list of billers");
        try {
            RestResponse response = billsService.billers();
            logInfo("Billers request completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Error processing billers request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Performs a bill inquiry operation.
     *
     * @param requestDTO The request data containing bill inquiry details
     * @return A RestResponse containing the result of the inquiry operation
     */
    @PostMapping("/inquiry")
    public RestResponse enquiry(@RequestBody @Valid BillsEnquiryRequestDTO requestDTO) {
        logInfo("Received bill inquiry request for account: " + requestDTO.getCustomerAccount());
        try {
            RestResponse response = billsService.inquiry(requestDTO);
            logInfo("Bill inquiry request completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Error processing bill inquiry request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Confirms a bill payment operation.
     *
     * @param requestDTO The request data containing bill confirmation details
     * @return A RestResponse containing the result of the confirmation operation
     */
    @PostMapping("/confirm")
    public RestResponse confirm(@RequestBody @Valid BillsConfirmRequestDTO requestDTO) {
        logInfo("Received bill confirmation request for reference: " + requestDTO.getChannelRef());
        try {
            RestResponse response = billsService.confirm(requestDTO);
            logInfo("Bill confirmation request completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Error processing bill confirmation request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }

    /**
     * Processes a bill payment operation.
     *
     * @param requestDTO The request data containing bill payment details
     * @return A RestResponse containing the result of the payment operation
     */
    @PostMapping("/payment")
    public RestResponse transfer(@RequestBody @Valid BillsPaymentRequestDTO requestDTO) {
        logInfo("Received bill payment request for reference: " + requestDTO.getChannelRef());
        try {
            RestResponse response = billsService.payment(requestDTO);
            logInfo("Bill payment request completed with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Error processing bill payment request: " + e.getMessage());
            return exceptionResponse(e, null);
        }
    }
}
