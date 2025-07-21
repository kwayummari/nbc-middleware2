package com.itrust.middlewares.nbc.kycValidation.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.kycValidation.dtos.biometric.NIDABiometricRequestDTO;
import com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire.NIDAQuestionnaireRequestDTO;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.kycValidation.services.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/onboarding/kyc-validation")
@Tag(name = "KYC Validation Controller")
public class KYCValidationController extends BaseController {

    private final OnboardingService onboardingService;

    public KYCValidationController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @Operation(summary = "Questionnaire")
    @PostMapping("/questionnaire")
    public RestResponse questionnaire(@RequestBody @Valid NIDAQuestionnaireRequestDTO questionnaireRequestDTO,@Valid @RequestHeader("source") String source) {
        logInfo("Received questionnaire KYC request for NIN: " + questionnaireRequestDTO.getNin() + " from source: " + source);

        if(questionnaireRequestDTO.getNin().length() != 20){
            Map<String, Object> data = new HashMap<>();
            data.put("nin", questionnaireRequestDTO.getNin());
            logWarn("Invalid NIN length: " + questionnaireRequestDTO.getNin().length() + " for NIN: " + questionnaireRequestDTO.getNin());
            return errorResponse("Invalid NIN","602" ,null,data);
        }

        RestResponse response = onboardingService.questionnaireKYC(questionnaireRequestDTO,source);
        logInfo("Completed questionnaire KYC request for NIN: " + questionnaireRequestDTO.getNin() + " with status: " + (response.getStatus() ? "success" : "failure"));
        return response;

    }

    @Operation(summary = "Biometric")
    @PostMapping("/biometric")
    public RestResponse biometric(@RequestBody @Valid NIDABiometricRequestDTO nidaBiometricRequestDTO,@Valid @RequestHeader("source") String source) {
        logInfo("Received biometric KYC request for NIN: " + nidaBiometricRequestDTO.getNin() + " from source: " + source);

        RestResponse response = onboardingService.biometricKYC(nidaBiometricRequestDTO,source);
        logInfo("Completed biometric KYC request for NIN: " + nidaBiometricRequestDTO.getNin() + " with status: " + (response.getStatus() ? "success" : "failure"));
        return response;
    }
}
