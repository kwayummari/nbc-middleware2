package com.itrust.middlewares.nbc.modules.dpworld.controller;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.modules.bills.services.BillsService;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsConfirmRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsEnquiryRequestDTO;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillsPaymentRequestDTO;
import com.itrust.middlewares.nbc.modules.dpworld.dto.*;
import com.itrust.middlewares.nbc.modules.dpworld.service.DpWorldsService;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling bill-related HTTP requests.
 * This controller provides endpoints for retrieving billers, bill inquiries, confirmations, and payments.
 * It delegates the business logic to the BillsService.
 */
@RestController
@RequestMapping("/dpw")
@Validated
public class DPWorldController extends BaseController {

    private final DpWorldsService billsService;

    /**
     * Constructs a new BillsController with the required dependencies.
     *
     * @param billsService Service for handling bill-related operations
     */
    public DPWorldController(DpWorldsService billsService) {
        this.billsService = billsService;
        logInfo("BillsController initialized");
    }

    /**
     * Performs a bill inquiry operation.
     *
     * @param requestDTO The request data containing bill inquiry details
     * @return A RestResponse containing the result of the inquiry operation
     */
    @PostMapping("/inquiry")
    public GenericRestResponse<?> enquiry(@RequestBody @Valid DPOInquiryRequestDto requestDTO) throws Exception {
        logInfo("Received bill inquiry request for account: " + requestDTO.getCustomerAccount());
        return billsService.inquiry(requestDTO);
    }

    @PostMapping("/confirm")
    public GenericRestResponse<?> confirm(@RequestBody @Valid DOPOtpVerificationRequestDto requestDTO) throws Exception {
        return billsService.confirm(requestDTO);
    }

    @PostMapping("/payment")
    public GenericRestResponse<?> payment(@RequestBody @Valid DPOPaymentRequestDto requestDTO) throws Exception {
        return billsService.payment(requestDTO);
    }


}
