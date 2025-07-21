package com.itrust.middlewares.nbc.otp.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OTPVerifyResponseDTO {

    private int statusCode;
    private String message;
    private OTPVerifyResponseDataDTO data;
    private String error;
}
