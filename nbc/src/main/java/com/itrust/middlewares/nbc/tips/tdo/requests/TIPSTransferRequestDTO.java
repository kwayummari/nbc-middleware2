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
 * Data Transfer Object for TIPS transfer requests.
 * This class encapsulates all the necessary information required to perform a funds transfer
 * operation in the Tanzania Instant Payment System (TIPS) after confirmation.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TIPSTransferRequestDTO implements Serializable {

    /**
     * The channel reference number that uniquely identifies the transaction.
     * This reference is used to link the transfer to the original lookup and confirmation requests.
     * This field is required.
     */
    @NotBlank
    private String channelRef;

    /**
     * The customer's reference for the transaction.
     * This is typically a unique identifier provided by the customer.
     * This field is required.
     */
    @NotBlank
    private String customerRef;

    /**
     * Additional remarks or notes provided by the customer for the transaction.
     * This field is required.
     */
    @NotBlank
    private String customerRemarks;

    /**
     * The verification code used to authenticate the transfer.
     * This field is required.
     */
    @NotBlank
    private String verificationCode;

    /**
     * The phone number of the payer (sender) for communication purposes.
     * This field is required.
     */
    @NotBlank
    private String payerPhoneNumber;

    /**
     * The email address of the payer (sender) for communication purposes.
     * This field is required.
     */
    @NotBlank
    private String payerEmail;

    /**
     * The URL to which TIPS will send callback notifications about the transfer status.
     * This field is set automatically by the service and does not require client input.
     */
    private String callbackUrl;

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
