package com.itrust.middlewares.nbc.otp.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerifyResponseDataDTO {

    public boolean verified;

    public String description;

    public String copreference;

    @Override
    public String toString() {
        return "{" +
                "verified=" + verified +
                ", description='" + description + '\'' +
                ", copreference='" + copreference + '\'' +
                '}';
    }
}
