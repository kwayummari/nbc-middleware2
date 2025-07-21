package com.itrust.middlewares.nbc.auth.dto.gateway;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
@Hidden
public class ClientRegistrationRequest {
    
    @NotBlank
    private String clientId;

    @NotBlank
    private String clientSecret;

    @NotBlank
    private String clientName;

    @NotBlank
    private Set<String> authenticationMethod;

    private Set<String> grantTypes;
    private Set<String> scopes;
    private Set<String> redirectUris;

}
