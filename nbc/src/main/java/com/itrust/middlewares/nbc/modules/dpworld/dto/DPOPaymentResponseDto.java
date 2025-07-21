package com.itrust.middlewares.nbc.modules.dpworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DPOPaymentResponseDto implements java.io.Serializable {
    private int statusCode;
    private String message;
    private String channelRef;
    private double totalCharges;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
