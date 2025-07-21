package com.itrust.middlewares.nbc.gepg.controller;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.gepg.requests.ConfirmDTO;
import com.itrust.middlewares.nbc.gepg.requests.InquiryDTO;
import com.itrust.middlewares.nbc.gepg.requests.PaymentDTO;
import com.itrust.middlewares.nbc.gepg.service.GepgService;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling Government Electronic Payment Gateway (GePG) operations.
 * 
 * This controller provides endpoints for bill inquiry, payment confirmation, and payment processing
 * through the GePG system. It extends the BaseController to leverage common logging and response
 * handling capabilities.
 * 
 * @author iTrust
 * @version 1.0
 */
@RestController
@RequestMapping("/gepg")
@Validated
public class GepgController extends BaseController {

    /** Service for handling GePG operations */
    private final GepgService gepgService;

    /**
     * Constructor for GepgController.
     * 
     * @param gepgService Service for handling GePG operations
     */
    public GepgController(GepgService gepgService) {
        this.gepgService = gepgService;
        logInfo("GepgController initialized");
    }

    /**
     * Endpoint for processing GePG bill inquiry requests.
     * 
     * This endpoint validates the incoming request and delegates to the GepgService
     * for processing the bill inquiry.
     *
     * @param inquiryDTO The inquiry request data transfer object
     * @return A GenericRestResponse containing the inquiry result or error information
     */
    @PostMapping("/inquiry")
    public GenericRestResponse<?> inquiry(@RequestBody @Valid InquiryDTO inquiryDTO) {
        logInfo("Received GePG inquiry request for account: " + inquiryDTO.getCustomerAccount());
        try {
            // Delegate to service layer for processing
            GenericRestResponse<?> response = gepgService.inquiry(inquiryDTO);
            logInfo("Completed processing GePG inquiry request for account: " + inquiryDTO.getCustomerAccount());
            return response;
        } catch (Exception e) {
            // Log the exception and return a standardized error response
            logError("Error processing GePG inquiry request: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for processing GePG payment confirmation requests.
     * 
     * This endpoint validates the incoming request and delegates to the GepgService
     * for processing the payment confirmation.
     *
     * @param confirmDTO The confirmation request data transfer object
     * @return A GenericRestResponse containing the confirmation result or error information
     */
    @PostMapping("/confirm")
    public GenericRestResponse<?> confirm(@RequestBody @Valid ConfirmDTO confirmDTO) {
        logInfo("Received GePG confirmation request for channel reference: " + confirmDTO.getChannelRef());
        try {
            // Delegate to service layer for processing
            GenericRestResponse<?> response = gepgService.confirm(confirmDTO);
            logInfo("Completed processing GePG confirmation request for channel reference: " + confirmDTO.getChannelRef());
            return response;
        } catch (Exception e) {
            // Log the exception and return a standardized error response
            logError("Error processing GePG confirmation request: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

    /**
     * Endpoint for processing GePG payment requests.
     * <p>
     * This endpoint validates the incoming request and delegates to the GepgService
     * for processing the payment.
     *
     * @param paymentDTO The payment request data transfer object
     * @return A GenericRestResponse containing the payment result or error information
     */
    @PostMapping("/payment")
    public GenericRestResponse<?> payment(@RequestBody @Valid PaymentDTO paymentDTO) {
        logInfo("Received GePG payment request for channel reference: " + paymentDTO.getChannelRef());
        try {
            // Delegate to the service layer for processing
            GenericRestResponse<?> response = gepgService.payment(paymentDTO);
            logInfo("Completed processing GePG payment request for channel reference: " + paymentDTO.getChannelRef());
            return response;
        } catch (Exception e) {
            // Log the exception and return a standardized error response
            logError("Error processing GePG payment request: " + e.getMessage());
            return exceptionResponseGeneric(e, null);
        }
    }

}
