package com.itrust.middlewares.nbc.tips.tdo.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Data Transfer Object for TIPS confirmation requests.
 * This class encapsulates the information required to confirm a transaction
 * in the Tanzania Instant Payment System (TIPS) after a successful lookup.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TIPSConfirmRequestDTO implements Serializable {

    /**
     * The channel reference number that uniquely identifies the transaction.
     * This reference is used to link the confirmation to the original lookup request.
     * This field is required.
     */
    @NotBlank
    private String channelRef;

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
