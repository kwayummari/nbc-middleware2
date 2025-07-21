package com.itrust.middlewares.nbc.auth.dto.responses;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessToken {
    
    @NotBlank
    private String access_token;

    @NotBlank
    private String scope;

    @NotBlank
    private String token_type;

    @NotBlank
    private int expires_in;

}
