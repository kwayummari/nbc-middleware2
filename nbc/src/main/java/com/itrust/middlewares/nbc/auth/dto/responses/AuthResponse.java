package com.itrust.middlewares.nbc.auth.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    AuthResponseHeaderDTO header;
    ArrayList<AuthResponseServiceDTO> services = new ArrayList<>();
    private boolean firstLogin;
    private boolean enabled;
    private boolean pinChangeRequired;
    private String username;
    private String accountNumber;
    private String language;
    private String endpoint;
    private String retries;
}
