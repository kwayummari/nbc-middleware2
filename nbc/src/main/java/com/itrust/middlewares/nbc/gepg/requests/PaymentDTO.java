package com.itrust.middlewares.nbc.gepg.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Data Transfer Object for GePG payment requests.
 * <p>
 * This class represents the request payload for processing a payment in the
 * Government Electronic Payment Gateway (GePG) system. It contains all the necessary
 * information to complete a payment transaction.
 * 
 * @author iTrust
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PaymentDTO implements Serializable {

    /** The channel reference number for the payment */
    private String channelRef;

    /** The Central Bank Payment Gateway reference number */
    private String cbpGwRef;

    /** The control number of the bill to be paid */
    private String controlNumber;

    /** The status code of the bill */
    private String billStsCode;

    /** The type of payment */
    private String payType;

    /** The amount to be paid */
    private String amount;

    /** The currency of the payment */
    private String currency;

    /** The customer account associated with the payment */
    private String customerAccount;

    /** The verification code for the payment */
    private String verificationCode;

    /** The list of payment details */
    private List<PmtDtls> pmtDtls;

    private String callbackUrl;

    @Override
    public String toString() {
       ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
