package com.itrust.middlewares.nbc.gepg.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for GePG payment confirmation requests.
 * 
 * This class represents the request payload for confirming a payment in the
 * Government Electronic Payment Gateway (GePG) system.
 * 
 * @author iTrust
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmDTO implements Serializable {

    /** The channel reference number for the payment */
    public String channelRef;

    /** The amount to be paid */
    public String amount;

    @Override
    public String toString() {
        return "{" +
                "channelRef='" + channelRef + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
