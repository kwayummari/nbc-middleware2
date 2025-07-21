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
 * Data Transfer Object for TIPS lookup requests.
 * This class encapsulates all the necessary information required to perform a lookup
 * operation in the Tanzania Instant Payment System (TIPS).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TIPSLookupRequestDTO implements Serializable {

    /**
     * The type of identifier used for the payee (e.g., "ACCOUNT", "MOBILE", etc.).
     * This field is required.
     */
    @NotBlank
    private String payeeIdentifierType;

    /**
     * The actual identifier value for the payee (e.g., account number, mobile number).
     * This field is required.
     */
    @NotBlank
    private String payeeIdentifier;

    /**
     * The Financial Service Provider ID of the payee.
     * This field is required.
     */
    @NotBlank
    private String payeeFspId;

    /**
     * The account number of the customer initiating the transaction.
     * This field is required.
     */
    @NotBlank
    private String customerAccount;

    /**
     * The amount to be transferred in the transaction.
     * This field is required.
     */
    @NotBlank
    private String amount;

    /**
     * The currency code for the transaction (e.g., "TZS" for Tanzanian Shilling).
     * This field is required.
     */
    @NotBlank
    private String currency;

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
