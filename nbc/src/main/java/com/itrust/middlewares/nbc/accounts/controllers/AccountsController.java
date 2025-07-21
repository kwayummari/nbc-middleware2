package com.itrust.middlewares.nbc.accounts.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.accounts.dtos.requests.*;
import com.itrust.middlewares.nbc.accounts.services.AccountsService;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling account-related operations.
 * This controller provides endpoints for account opening, status checking,
 * statement retrieval, and account inquiries.
 * 
 * @author iTrust Middleware Team
 * @version 1.0
 */
@RestController
@RequestMapping("/accounts")
@Validated
public class AccountsController extends BaseController {

    private final AccountsService accountsService;

    /**
     * Constructor for AccountsController.
     * 
     * @param accountsService The service responsible for account operations
     */
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * Endpoint for opening a new account.
     * 
     * @param requestDTO The request containing account opening details
     * @return RestResponse containing the result of the account opening operation
     */
    @PostMapping("/opening")
    public RestResponse opening(@RequestBody @Valid AccountOpenRequestDTO requestDTO) {
       logInfo("Received request to open a new account with NIN: " + requestDTO.getNin());
       try {
           RestResponse response = accountsService.openAccount(requestDTO);
           logInfo("Account opening request processed successfully");
           return response;
       } catch (Exception e) {
           logError("Error occurred while processing account opening request: " + e.getMessage());
           return exceptionResponse(e,null);
       }
    }

    /**
     * Endpoint for checking account status.
     * 
     * @param requestDTO The request containing account status query details
     * @return RestResponse containing the account status information
     */
    @PostMapping("/account-status")
    public RestResponse accountStatus(@RequestBody @Valid AccountStatusRequestDTO requestDTO) {
       logInfo("Received request to check account status for ID number: " + requestDTO.getIdNumber());
       try {
           RestResponse response = accountsService.accountStatus(requestDTO);
           logInfo("Account status request processed successfully");
           return response;
       } catch (Exception e) {
           logError("Error occurred while processing account status request: " + e.getMessage());
           return exceptionResponse(e,null);
       }
    }

    /**
     * Endpoint for retrieving a mini statement for an account.
     * 
     * @param requestDTO The request containing mini statement query details
     * @param source The source of the request (e.g., "itrust")
     * @return RestResponse containing the mini statement information
     */
    @PostMapping("/account-mini-statement")
    public RestResponse accountMiniStatement(@RequestBody @Valid AccountMiniStatementRequestDTO requestDTO, @RequestHeader String source) {
       logInfo("Received request for mini statement for account: " + requestDTO.getAccountNumber() + " from source: " + source);
       try {
           RestResponse response = accountsService.accountMiniStatement(requestDTO, source);
           logInfo("Account mini statement request processed successfully");
           return response;
       } catch (Exception e) {
           logError("Error occurred while processing account mini statement request: " + e.getMessage());
           return exceptionResponse(e,null);
       }
    }

    /**
     * Endpoint for retrieving a full statement for an account.
     * 
     * @param requestDTO The request containing full statement query details
     * @return RestResponse containing the full statement information
     */
    @PostMapping("/account-full-statement")
    public RestResponse accountFullStatement(@RequestBody @Valid AccountFullStatementRequestDTO requestDTO) {
       logInfo("Received request for full statement for account: " + requestDTO.getAccountNumber());
       try {
           RestResponse response = accountsService.accountFullStatement(requestDTO);
           logInfo("Account full statement request processed successfully");
           return response;
       } catch (Exception e) {
           logError("Error occurred while processing account full statement request: " + e.getMessage());
           return exceptionResponse(e,null);
       }
    }

    /**
     * Endpoint for account inquiry/balance check.
     * 
     * @param requestDTO The request containing account inquiry details
     * @return RestResponse containing the account inquiry information
     */
    @PostMapping("/enquiry")
    public RestResponse enquiry(@RequestBody @Valid AccountEnquiryRequestDTO requestDTO) {
       logInfo("Received account inquiry request for account: " + requestDTO.getAccountNumber());
       try {
           RestResponse response = accountsService.accountEnquiry(requestDTO);
           logInfo("Account inquiry request processed successfully");
           return response;
       } catch (Exception e) {
           logError("Error occurred while processing account inquiry request: " + e.getMessage());
           return exceptionResponse(e,null);
       }
    }

    /**
     * Endpoint for retrieving available account options.
     * 
     * @return RestResponse containing the available account options
     */
    @GetMapping("/options")
    public RestResponse options() {
        logInfo("Received request for account options");
        try {
            RestResponse response = accountsService.options();
            logInfo("Account options request processed successfully");
            return response;
        } catch (Exception e) {
            logError("Error occurred while processing account options request: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

}
