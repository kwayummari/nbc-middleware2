package com.itrust.middlewares.nbc.luku.controller;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.luku.requests.ConfirmDTO;
import com.itrust.middlewares.nbc.luku.requests.InquiryDTO;
import com.itrust.middlewares.nbc.luku.requests.PaymentDTO;
import com.itrust.middlewares.nbc.luku.service.LukuService;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling LUKU (prepaid electricity) operations.
 * This controller provides endpoints for meter inquiry, payment confirmation,
 * and processing payments for LUKU services.
 * 
 * @author iTrust Team
 * @version 1.0
 */
@RestController
@RequestMapping("/luku")
@Validated
public class LukuController extends BaseController {

    /** Service for handling LUKU operations */
    private final LukuService service;

    /**
     * Constructor for LukuController.
     * 
     * @param service The service that handles LUKU operations
     */
    public LukuController(LukuService service) {
        this.service = service;
    }

    /**
     * Endpoint for LUKU meter inquiry.
     * This endpoint allows clients to retrieve information about a LUKU meter.
     * 
     * @param inquiryDTO The inquiry request data containing meter number and customer account
     * @return GenericRestResponse containing the inquiry result or error information
     */
    @PostMapping("/inquiry")
    public GenericRestResponse<?> inquiry(@RequestBody @Valid InquiryDTO inquiryDTO) {
        logInfo("Received LUKU inquiry request for meter: " + inquiryDTO.getMeter() + 
                ", account: " + inquiryDTO.getCustomerAccount());
        try {
            GenericRestResponse<?> response = service.inquiry(inquiryDTO);
            logInfo("Completed LUKU inquiry request with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Exception occurred while processing LUKU inquiry request: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for confirming a LUKU payment transaction.
     * This endpoint allows clients to confirm and finalize a LUKU payment.
     * 
     * @param confirmDTO The confirmation request data containing channel reference and amount
     * @return GenericRestResponse containing the confirmation result or error information
     */
    @PostMapping("/confirm")
    public GenericRestResponse<?> confirm(@RequestBody @Valid ConfirmDTO confirmDTO) {
        logInfo("Received LUKU confirmation request for channel reference: " + confirmDTO.getChannelRef() + 
                ", amount: " + confirmDTO.getAmount());
        try {
            GenericRestResponse<?> response = service.confirm(confirmDTO);
            logInfo("Completed LUKU confirmation request with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Exception occurred while processing LUKU confirmation request: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for processing a LUKU payment transaction.
     * This endpoint allows clients to make payments for LUKU services.
     * 
     * @param paymentDTO The payment request data containing customer and transaction details
     * @return GenericRestResponse containing the payment result or error information
     */
    @PostMapping("/payment")
    public GenericRestResponse<?> payment(@RequestBody @Valid PaymentDTO paymentDTO) {
        logInfo("Received LUKU payment request for channel reference: " + paymentDTO.getChannelRef() + 
                ", customer: " + paymentDTO.getCustomerName());
        try {
            GenericRestResponse<?> response = service.payment(paymentDTO);
            logInfo("Completed LUKU payment request with status: " + (response.getStatus() ? "SUCCESS" : "FAILED"));
            return response;
        } catch (Exception e) {
            logError("Exception occurred while processing LUKU payment request: " + e.getMessage());
            logger.error("Exception details:", e);
            return exceptionResponseGeneric(e, null);
        }
    }

}
