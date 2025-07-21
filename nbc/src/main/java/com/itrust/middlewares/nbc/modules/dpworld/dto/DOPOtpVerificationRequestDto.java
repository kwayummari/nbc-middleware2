package com.itrust.middlewares.nbc.modules.dpworld.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DOPOtpVerificationRequestDto implements Serializable {
    private String channelRef;
    private double amount;

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
