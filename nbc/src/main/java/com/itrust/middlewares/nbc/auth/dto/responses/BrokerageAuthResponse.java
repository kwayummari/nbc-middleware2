package com.itrust.middlewares.nbc.auth.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokerageAuthResponse {

    private String token_type;
    private String expires_in;
    private String access_token;
    private String refresh_token;
    private String message;
}
