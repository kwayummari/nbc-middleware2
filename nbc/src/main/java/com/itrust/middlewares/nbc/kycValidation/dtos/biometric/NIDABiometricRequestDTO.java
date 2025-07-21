package com.itrust.middlewares.nbc.kycValidation.dtos.biometric;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NIDABiometricRequestDTO implements Serializable {

    @NotBlank
    private String nin;

    @NotNull
    private String idType;

    @NotBlank
    private String fingerImage;

    @NotBlank
    private String fingerCode;

    @Override
    public String toString() {
        return "{" +
                "nin='" + nin + '\'' +
                ", idType='" + idType + '\'' +
                ", fingerImage='" + fingerImage + '\'' +
                ", fingerCode='" + fingerCode + '\'' +
                '}';
    }

}
