package com.itrust.middlewares.nbc.auth.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccessToken {

    @NotBlank
    @JsonProperty("access_token")
    private String accessToken;

    @NotBlank
    @JsonProperty("scope")
    private String scope;

    @NotBlank
    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private int expiresIn;
}
