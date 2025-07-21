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
public class BillsPaymentRequestDTO implements Serializable {

    @NotBlank
    private String channelRef;

    @NotBlank
    private String verificationCode;

    @NotBlank
    private String payerPhone;

    @NotBlank
    private String payerEmail;

    @NotBlank
    private String payerName;

    @NotBlank
    private String narration;

    private Object extraFields;

    @Override
    public String toString() {
        return "{" +
                "channelRef='" + channelRef + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", payerPhone='" + payerPhone + '\'' +
                ", payerEmail='" + payerEmail + '\'' +
                ", payerName='" + payerName + '\'' +
                ", narration='" + narration + '\'' +
                ", extraFields=" + extraFields +
                '}';
    }
}
