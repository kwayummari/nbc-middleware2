package com.itrust.middlewares.nbc.auth.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexAuthResponse {

    private String token;
    private String message;
}
