package com.itrust.middlewares.nbc.otp.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.otp.dto.requests.OTPSendRequestDTO;
import com.itrust.middlewares.nbc.otp.dto.requests.OTPVerifyRequestDTO;
import com.itrust.middlewares.nbc.otp.services.OTPService;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class OTPController extends BaseController {

    private final OTPService OTPService;

    public OTPController(OTPService OTPService) {
        this.OTPService = OTPService;
    }

    @PostMapping("/otp/send")
    public RestResponse otpSend(@RequestBody @Valid OTPSendRequestDTO requestDTO,@RequestHeader("source") String source) {
        try {
            return OTPService.OTPSend(requestDTO,source);
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/otp/verify")
    public RestResponse otpVerify(@RequestBody @Valid OTPVerifyRequestDTO requestDTO,@RequestHeader("source") String source) {
        try {
            return OTPService.OTPVerify(requestDTO,source);
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

}
