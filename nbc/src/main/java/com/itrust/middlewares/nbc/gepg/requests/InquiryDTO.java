package com.itrust.middlewares.nbc.gepg.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for GePG bill inquiry requests.
 * 
 * This class represents the request payload for inquiring about a bill in the
 * Government Electronic Payment Gateway (GePG) system.
 * 
 * @author iTrust
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InquiryDTO implements Serializable {

    /** The control number of the bill to inquire about */
    public String controlNumber;

    /** The customer account associated with the bill */
    public String customerAccount;

    @Override
    public String toString() {
        return "{" +
                "controlNumber='" + controlNumber + '\'' +
                ", customerAccount='" + customerAccount + '\'' +
                '}';
    }
}
