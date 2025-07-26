package com.itrust.middlewares.nbc.auth.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.auth.dto.responses.AccessToken;
import com.itrust.middlewares.nbc.config.properties.AuthServerProperties;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GatewayAuthService {

    private final StringRedisTemplate redisTemplate;
    private static final String TOKEN_CACHE_KEY = "api_gateway:access_token";
    private static final long TOKEN_EXPIRY_BUFFER = 30; // seconds buffer before expiry
    private final AuthServerProperties authServerProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GatewayAuthService(StringRedisTemplate redisTemplate, AuthServerProperties authServerProperties) {
        this.redisTemplate = redisTemplate;
        this.authServerProperties = authServerProperties;
    }

    public String getAccessToken() throws JsonProcessingException {
        String cachedToken = redisTemplate.opsForValue().get(TOKEN_CACHE_KEY);
        if (cachedToken != null && !cachedToken.isEmpty()) {
            return cachedToken;
        }

        return requestNewToken();
    }

    public String requestNewToken() throws JsonProcessingException {
        String tokenResponse = fetchTokenFromAuthServer(); // Raw JSON string

        log.info("Token response: {}", tokenResponse);

        AccessToken token = objectMapper.readValue(tokenResponse, AccessToken.class);

        if (token != null && token.getAccessToken() != null) {
            String accessToken = token.getAccessToken();
            int expiresIn = token.getExpiresIn();

            // Save in Redis with buffer time
            redisTemplate.opsForValue().set(TOKEN_CACHE_KEY, accessToken, expiresIn - TOKEN_EXPIRY_BUFFER, TimeUnit.SECONDS);
            return accessToken;
        } else {
            log.error("Error fetching token or token is null");
            return "";
        }
    }


    private String fetchTokenFromAuthServer() {
        String body = "grant_type=client_credentials&scope=funds_read";

        String basicAuth = Base64.getEncoder().encodeToString(
                (authServerProperties.getClientId() + ":" + authServerProperties.getClientSecret()).getBytes()
        );

        return RestClient.create()
                .post()
                .uri(authServerProperties.getIssuerUri() + "/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic " + basicAuth)
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
