package com.itrust.middlewares.nbc.transfers.controller;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import com.itrust.middlewares.nbc.transfers.dto.TransferConfirmRequestDTO;
import com.itrust.middlewares.nbc.transfers.dto.TransferInquiryRequestDTO;
import com.itrust.middlewares.nbc.transfers.dto.TransferVerifyRequestDTO;
import com.itrust.middlewares.nbc.transfers.services.TransfersService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for handling transfer-related operations.
 * <p>
 * This controller provides endpoints for the three main steps in the transfer process:
 * 1. Inquiry - Initial request to check transfer details
 * 2. Verification - Verify the transfer details before proceeding
 * 3. Confirmation - Final step to execute the transfer
 * <p>
 * All endpoints validate the incoming request data and delegate processing to the TransfersService.
 */
@RestController
@RequestMapping("/transfers")
@Validated
public class TransfersController extends BaseController {

    /** Service responsible for handling transfer operations */
    private final TransfersService transfersService;

    /**
     * Constructs a new TransfersController with the required dependencies.
     *
     * @param transfersService Service for handling transfer operations
     */
    public TransfersController(TransfersService transfersService) {
        this.transfersService = transfersService;
        logInfo("TransfersController initialized");
    }

    /**
     * Endpoint for initiating a transfer inquiry.
     * This is typically the first step in the transfer process to check transfer details.
     *
     * @param requestDTO The transfer inquiry request data
     * @return A GenericRestResponse containing the result of the inquiry
     */
    @PostMapping("/inquiry")
    public GenericRestResponse<?> enquiry(@RequestBody @Valid TransferInquiryRequestDTO requestDTO) {
        logInfo("Received transfer inquiry request");
        try {
            logInfo("Delegating to TransfersService.inquiry");
            GenericRestResponse<?> response = transfersService.inquiry(requestDTO);
            logInfo("Transfer inquiry processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing transfer inquiry: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for verifying transfer details.
     * This is typically the second step in the transfer process after inquiry.
     *
     * @param requestDTO The transfer verification request data
     * @return A GenericRestResponse containing the result of the verification
     */
    @PostMapping("/verify")
    public GenericRestResponse<?> verify(@RequestBody @Valid TransferVerifyRequestDTO requestDTO) {
        logInfo("Received transfer verification request");
        try {
            logInfo("Delegating to TransfersService.verify");
            GenericRestResponse<?> response = transfersService.verify(requestDTO);
            logInfo("Transfer verification processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing transfer verification: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for confirming and executing a transfer.
     * This is typically the final step in the transfer process after inquiry and verification.
     *
     * @param requestDTO The transfer confirmation request data
     * @return A GenericRestResponse containing the result of the confirmation
     */
    @PostMapping("/confirm")
    public GenericRestResponse<?> confirm(@RequestBody @Valid TransferConfirmRequestDTO requestDTO) {
        logInfo("Received transfer confirmation request");
        try {
            logInfo("Delegating to TransfersService.confirm");
            GenericRestResponse<?> response = transfersService.confirm(requestDTO);
            logInfo("Transfer confirmation processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error processing transfer confirmation: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }
}
