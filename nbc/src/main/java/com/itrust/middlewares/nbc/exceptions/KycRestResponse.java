package com.itrust.middlewares.nbc.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.kycValidation.dtos.NidaResponseFullDTO;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KycRestResponse {

    @Setter
    private String timestamp;
    @Setter
    private Boolean status;
    @Setter
    private String statusCode;
    @Setter
    private String message;

    private Map<String, Object> messages;

    private NidaResponseFullDTO data;

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
