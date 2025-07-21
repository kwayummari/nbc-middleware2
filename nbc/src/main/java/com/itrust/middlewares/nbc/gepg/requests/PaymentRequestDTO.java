package com.itrust.middlewares.nbc.gepg.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PaymentRequestDTO implements Serializable {

    String channelRef;

    String cbpGwRef;

    String controlNumber;

    String billStsCode;

    String payType;

    String amount;

    String currency;

    String customerAccount;

    String verificationCode;

    List<PmtDtls> pmtDtls;

    @Override
    public String toString() {
        return "{" +
                "channelRef='" + channelRef + '\'' +
                ", cbpGwRef='" + cbpGwRef + '\'' +
                ", controlNumber='" + controlNumber + '\'' +
                ", billStsCode='" + billStsCode + '\'' +
                ", payType='" + payType + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", customerAccount='" + customerAccount + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", pmtDtls=" + pmtDtls +
                '}';
    }
}
