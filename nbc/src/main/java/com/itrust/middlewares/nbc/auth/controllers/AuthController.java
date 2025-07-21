package com.itrust.middlewares.nbc.auth.controllers;

import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.auth.dto.requests.AuthChangePasswordRequestDTO;
import com.itrust.middlewares.nbc.auth.dto.requests.AuthLoginRequestDTO;
import com.itrust.middlewares.nbc.auth.services.AuthService;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public RestResponse login() {
        try {
            return authService.authenticate();
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }

    @PostMapping("/change-password")
    public RestResponse changePassword(@RequestBody @Valid AuthChangePasswordRequestDTO authChangePasswordRequestDTO) {
        try {
            return authService.changePassword(authChangePasswordRequestDTO);
        } catch (Exception e) {
            return exceptionResponse(e,null);
        }
    }
}
