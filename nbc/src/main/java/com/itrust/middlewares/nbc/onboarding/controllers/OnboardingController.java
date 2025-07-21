package com.itrust.middlewares.nbc.onboarding.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.accounts.dtos.callback.AccountOpenCallbackRequestDTO;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.onboarding.requests.AccountDetailsDTO;
import com.itrust.middlewares.nbc.onboarding.requests.AccountSyncDTO;
import com.itrust.middlewares.nbc.onboarding.requests.CreateAccountDTO;
import com.itrust.middlewares.nbc.onboarding.requests.CreateAccountRetryDTO;
import com.itrust.middlewares.nbc.onboarding.services.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/onboarding")
public class OnboardingController extends BaseController {

    private final RestTemplate restTemplate;
    private final OnboardingService onboardingService;

    @Value("${file.upload.base.directory:uploads}")
    private String baseDirectory;

    /**
     * Constructor for OnboardingController.
     *
     * @param restTemplate The RestTemplate instance used for making HTTP requests.
     * @param onboardingService The OnboardingService instance used for onboarding operations.
     */
    public OnboardingController(RestTemplate restTemplate, OnboardingService onboardingService) {
        this.restTemplate = restTemplate;
        this.onboardingService = onboardingService;
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("attachment") String attachment) {
        logInfo("Upload file request received for attachment: " + attachment);

        // Normalize and validate the file path to prevent path traversal
        Path basePath = Paths.get(baseDirectory).toAbsolutePath().normalize();
        Path normalizedPath = Paths.get(attachment).normalize();
        Path resolvedPath = basePath.resolve(normalizedPath).normalize();

        // Check if the resolved path is still within the base directory
        if (!resolvedPath.startsWith(basePath)) {
            logError("Path traversal attempt detected: " + attachment);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Invalid file path");
        }

        // Use the validated path
        File file = resolvedPath.toFile();
        if (!file.exists()) {
            logError("File not found: " + resolvedPath);
            return ResponseEntity.badRequest().body("File not found!");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("attachment", new FileSystemResource(resolvedPath.toString()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = "http://192.168.1.162:40414/v2/innova/proof-of-payment"; // Replace it with actual server URL
        logInfo("Sending file upload request to: " + serverUrl);
        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, String.class);
        logInfo("File upload response status: " + response.getStatusCode());

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/create-account")
    public RestResponse createAccount(@RequestBody @Valid CreateAccountDTO requestDTO,@RequestHeader("source") String source) {
        logInfo("Create account request received from source: " + source + ", NIN: " + requestDTO.getNin());
        try {
            RestResponse response = onboardingService.createAccount(requestDTO,source);
            logInfo("Create account response status: " + (response.getStatus() ? "Success" : "Failed") + 
                    ", code: " + response.getStatusCode() + ", message: " + response.getMessage());
            return response;
        } catch (Exception e) {
            logError("Error in create account: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/create-account-retry")
    public RestResponse createAccountRetry(@RequestBody @Valid CreateAccountRetryDTO requestDTO, @RequestHeader("source") String source) {
        logInfo("Create account retry request received from source: " + source + ", NIN: " + requestDTO.getNin());
        try {
            RestResponse response = onboardingService.createAccountRetry(requestDTO,source);
            logInfo("Create account retry response status: " + (response.getStatus() ? "Success" : "Failed") + 
                    ", code: " + response.getStatusCode() + ", message: " + response.getMessage());
            return response;
        } catch (Exception e) {
            logError("Error in create account retry: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/create-account-callback")
    public RestResponse accountCallBackProcess(@RequestBody @Valid AccountOpenCallbackRequestDTO requestDTO) {
        logInfo("Account callback process request received for reference: " + 
                (requestDTO.getBody() != null && requestDTO.getBody().getData() != null ? 
                requestDTO.getBody().getData().getCopreference() : "unknown"));
        try {
            RestResponse response = onboardingService.createAccountCallback(requestDTO);
            logInfo("Account callback process response status: " + (response.getStatus() ? "Success" : "Failed") + 
                    ", code: " + response.getStatusCode() + ", message: " + response.getMessage());
            return response;
        } catch (Exception e) {
            logError("Error in account callback process: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/account-syncing")
    public RestResponse accountSyncing(@RequestBody @Valid AccountSyncDTO requestDTO) {
        logInfo("Account syncing request received for NIN: " + requestDTO.getNin());
        try {
            RestResponse response = onboardingService.accountSyncSyncing(requestDTO);
            logInfo("Account syncing response status: " + (response.getStatus() ? "Success" : "Failed") + 
                    ", code: " + response.getStatusCode() + ", message: " + response.getMessage());
            return response;
        } catch (Exception e) {
            logError("Error in account syncing: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/account-details")
    public RestResponse accountDetails(@RequestBody @Valid AccountDetailsDTO requestDTO) {
        logInfo("Account details request received for NIN: " + requestDTO.getNin());
        try {
            RestResponse response = onboardingService.accountDetails(requestDTO);
            logInfo("Account details response status: " + (response.getStatus() ? "Success" : "Failed") + 
                    ", code: " + response.getStatusCode() + ", message: " + response.getMessage());
            return response;
        } catch (Exception e) {
            logError("Error in account details: " + e.getMessage());
            return exceptionResponse(e,null);
        }
    }

}
