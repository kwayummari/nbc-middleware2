package com.itrust.middlewares.nbc.auth.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponseHeaderDTO {
    private int statusCode;
    private String channel;
    private String timestamp; //2024-05-02 9:42:25
    private String message;
    private String token;
    private String refreshToken;
    private String expiredIn;
}
