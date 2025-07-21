package com.itrust.middlewares.nbc.caching.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataCaching {

    private String token;

    private String refreshToken;

    private String timestamp;

    @Override
    public String toString() {
        return "{" +
                "token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
