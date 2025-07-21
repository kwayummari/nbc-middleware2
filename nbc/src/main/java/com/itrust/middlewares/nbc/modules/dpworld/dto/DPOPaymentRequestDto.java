package com.itrust.middlewares.nbc.modules.dpworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DPOPaymentRequestDto implements Serializable {
    private String channelRef;
    private Map<String, String> extraFields; // Can hold dynamic key-value pairs
    private String narration;
    private String payerEmail;
    private String payerName;
    private String payerPhone;
    private String verificationCode;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";

        }
    }
}
