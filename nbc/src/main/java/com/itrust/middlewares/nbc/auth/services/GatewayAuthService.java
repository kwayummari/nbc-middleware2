package com.itrust.middlewares.nbc.auth.services;


import com.fasterxml.jackson.core.JsonProcessingException;
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

    private final StringRedisTemplate redisTemplate; // Spring Redis Template
    private static final String TOKEN_CACHE_KEY = "api_gateway:access_token";
    private static final long TOKEN_EXPIRY_BUFFER = 30; // Buffer time in seconds
    private final AuthServerProperties authServerProperties;

    public GatewayAuthService(StringRedisTemplate redisTemplate, AuthServerProperties authServerProperties) {
        this.redisTemplate = redisTemplate;
        this.authServerProperties = authServerProperties;
    }

    public String getAccessToken() throws JsonProcessingException {
        return requestNewToken();
    }

    public String requestNewToken() throws JsonProcessingException {
        String tokenResponse = fetchTokenFromAuthServer(); // Call OAuth2 server

        log.info("token response: {}", tokenResponse);

        GenericRestResponse<AccessToken> response = GenericRestResponse.fromJson(tokenResponse, new com.fasterxml.jackson.core.type.TypeReference<>() {});

        if(response != null) {
            if(response.getData() != null) {
                String accessToken = response.getData().getAccess_token() != null ? response.getData().getAccess_token() : "";
                int expiresIn = response.getData().getExpires_in();

                // Store token in Redis with expiry (minus buffer)
                redisTemplate.opsForValue().set(TOKEN_CACHE_KEY, accessToken, expiresIn - TOKEN_EXPIRY_BUFFER, TimeUnit.SECONDS);
                return accessToken;
            } else {
                log.error("Error fetching token: {}", response.getMessage());
            }
        }

        return "";
    }

    private String fetchTokenFromAuthServer() throws JsonProcessingException {

        String json = """
                {
                    "clientName": "iTrust Finance Limited",
                    "clientId": "yastanzania.client",
                    "clientSecret": "yasSecret.itrust123!",
                    "authenticationMethod": ["client_secret_basic"],
                    "grantTypes": ["client_credentials"],
                    "scopes": ["read", "write"],
                    "redirectUris": ["http://localhost:2020/callback"]
                }""";
//        ClientRegistrationRequest requestClient = objectMapper.readValue(json, ClientRegistrationRequest.class);
//        producerTemplate.requestBody("direct:clients-register", requestClient, GenericRestResponse.class);

        // Request new token
        String body = "grant_type=client_credentials&&scope=funds_read";
        // Send request to OAuth2 server
        return RestClient.create()
                .post()
                .uri(authServerProperties.getIssuerUri() + "/oauth2/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(("yastanzania.client:yasSecret.itrust123!").getBytes()))
                .body(body)
                .retrieve()
                .body(String.class);
    }
}
