package com.itrust.middlewares.nbc.onboarding.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSyncDTO implements Serializable {

    public String nin;

    public String statusCode;

    public String message;

    public String dseCds;

    public String dseCdsStatus;

    public String innovaClientCode;

    public String innovaClientCodeStatus;

    public String cbsCif;

    public String cbsCifStatus;

    public String cbsKyc;

    public String cbsKycStatus;

    public String cbsAcc;

    public String cbsAccStatus;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            
            return "{}"; // Return empty JSON if an error occurs
        }
    }
}
