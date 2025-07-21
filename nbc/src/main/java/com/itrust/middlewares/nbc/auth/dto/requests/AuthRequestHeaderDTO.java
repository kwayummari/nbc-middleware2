package com.itrust.middlewares.nbc.auth.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class AuthRequestHeaderDTO implements Serializable {

    private String channel;
    private String signature;
    private String thirdPartyId;
    private String authMode;
    private String sabpVersion;
    private String timestamp;
    private String deviceId;
    private String deviceName;

}
