package com.itrust.middlewares.nbc.luku.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for LUKU meter inquiry requests.
 * This class encapsulates the data required to perform a LUKU meter inquiry.
 * 
 * @author iTrust Team
 * @version 1.0
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InquiryDTO implements Serializable {

    /** The meter number to be queried */
    public String meter;

    /** The customer account associated with the meter */
    public String customerAccount;

    @Override
    public String toString() {
        return "{" +
                "meter='" + meter + '\'' +
                ", customerAccount='" + customerAccount + '\'' +
                '}';
    }
}
