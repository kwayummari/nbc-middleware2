package com.itrust.middlewares.nbc.modules.bills.tdo.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class BillsEnquiryRequestDTO implements Serializable {

    @NotBlank
    private String billRef;

    @NotBlank
    private String spCode;

    @NotBlank
    private String customerAccount;

    private Object extraFields;

    @Override
    public String toString() {
        return "{" +
                "billRef='" + billRef + '\'' +
                ", spCode='" + spCode + '\'' +
                ", customerAccount='" + customerAccount + '\'' +
                ", extraFields=" + extraFields +
                '}';
    }
}
