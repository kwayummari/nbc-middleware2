package com.itrust.middlewares.nbc.luku.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.itrust.middlewares.nbc.gepg.requests.PmtDtls;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PaymentDTO implements Serializable {

    String channelRef;

    String verificationCode;

    String customerEmail;

    String customerMsisdn;

    String customerNIN;

    String customerTIN;

    String customerName;

    Boolean waitToken;

    String callbackUrl;

    @Override
    public String toString() {
        return "{" +
                "            callbackUrl='" + callbackUrl + '\'' +
                ",             channelRef='" + channelRef + '\'' +
                ",             customerEmail='" + customerEmail + '\'' +
                ",             customerMsisdn='" + customerMsisdn + '\'' +
                ",             customerName='" + customerName + '\'' +
                ",             customerNIN='" + customerNIN + '\'' +
                ",             customerTIN='" + customerTIN + '\'' +
                ",             verificationCode='" + verificationCode + '\'' +
                ",             waitToken=" + waitToken +
                '}';
    }
}
