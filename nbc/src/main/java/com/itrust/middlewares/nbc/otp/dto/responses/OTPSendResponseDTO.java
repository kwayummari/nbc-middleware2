package com.itrust.middlewares.nbc.otp.dto.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OTPSendResponseDTO {
    public String phoneNumber;
    public String firstname;
    public String surname;
    public String emailAddress;
    public String COPreference;

    @Override
    public String toString() {
        return "{" +
                "COPreference='" + COPreference + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
