package com.itrust.middlewares.nbc.auth.dto.gateway;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Hidden
public class ClientTokenRequest {
    
    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    private String grantType;

    @NotBlank
    private String scope;
}
