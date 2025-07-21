package com.itrust.middlewares.nbc.auth.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrokerageAuthRequest {

    private String username;
    private String password;
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String scope;

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}
