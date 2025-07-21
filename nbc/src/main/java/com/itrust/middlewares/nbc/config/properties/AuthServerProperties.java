package com.itrust.middlewares.nbc.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "auth.server")
@Validated
@Data
public class AuthServerProperties {
    private String issuerUri;
    private String clientId;
    private String clientSecret;
}