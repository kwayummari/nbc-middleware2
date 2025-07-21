package com.itrust.middlewares.nbc.luku.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for LUKU payment confirmation requests.
 * This class encapsulates the data required to confirm a LUKU payment transaction.
 * 
 * @author iTrust Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmDTO implements Serializable {

    /** The reference ID for the transaction channel */
    public String channelRef;

    /** The payment amount to be confirmed */
    public String amount;

    @Override
    public String toString() {
        return "{" +
                "channelRef='" + channelRef + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
