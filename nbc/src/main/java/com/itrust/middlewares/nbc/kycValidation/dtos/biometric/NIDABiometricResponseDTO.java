package com.itrust.middlewares.nbc.kycValidation.dtos.biometric;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NIDABiometricResponseDTO {
    private int statusCode;
    private String message;
    private NIDABiometricResponseDataDTO data;
    private String error;
}
