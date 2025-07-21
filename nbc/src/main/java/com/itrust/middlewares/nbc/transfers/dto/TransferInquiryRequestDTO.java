package com.itrust.middlewares.nbc.transfers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferInquiryRequestDTO implements Serializable {

    @NotBlank
    private String senderAccount;

    @NotBlank
    private String receiverAccount;

    @NotBlank
    private String amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String narration;

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
