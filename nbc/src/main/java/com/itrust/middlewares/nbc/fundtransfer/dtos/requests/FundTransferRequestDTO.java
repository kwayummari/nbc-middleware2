package com.itrust.middlewares.nbc.fundtransfer.dtos.requests;

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
public class FundTransferRequestDTO implements Serializable {

    @NotBlank
    private String spCode;

    @NotBlank
    private String channelRef;

    @NotBlank
    private String billRef;

    @NotBlank
    private String approach;

    @NotBlank
    private String amount;

    @NotBlank
    private String creditAccount;

    @NotBlank
    private String creditCurrency;

    @NotBlank
    private String debitAccount;

    @NotBlank
    private String debitCurrency;

    @NotBlank
    private String payerName;

    @NotBlank
    private String payerPhone;

    @NotBlank
    private String payerEmail;

    @NotBlank
    private String narration;

    private String callbackUrl;

    @NotBlank
    private String requestType;

    @NotBlank
    private String paymentType;


    private Object extraFields;

    @Override
    public String toString() {
        return "{" +
                "spCode='" + spCode + '\'' +
                ", channelRef='" + channelRef + '\'' +
                ", billRef='" + billRef + '\'' +
                ", approach='" + approach + '\'' +
                ", amount='" + amount + '\'' +
                ", creditAccount='" + creditAccount + '\'' +
                ", creditCurrency='" + creditCurrency + '\'' +
                ", debitAccount='" + debitAccount + '\'' +
                ", debitCurrency='" + debitCurrency + '\'' +
                ", payerName='" + payerName + '\'' +
                ", payerPhone='" + payerPhone + '\'' +
                ", payerEmail='" + payerEmail + '\'' +
                ", narration='" + narration + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", requestType='" + requestType + '\'' +
                ", extraFields=" + extraFields +
                '}';
    }
}
