package com.itrust.middlewares.nbc.config;

import com.itrust.middlewares.nbc.auth.services.GatewayAuthService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

@Slf4j
public class NBCTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final GatewayAuthService authService;

    public NBCTemplateInterceptor(GatewayAuthService authService) {
        this.authService = authService;
    }

    @Override
    public @NotNull ClientHttpResponse intercept(HttpRequest request, byte @NotNull[] body, ClientHttpRequestExecution execution) throws IOException {

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);

        try {
            String token = request.getHeaders().getFirst("Authorization");

            if (token == null || token.isEmpty()) {
                // No Authorization header, get a new token
                token = authService.getAccessToken();
                requestWrapper.getHeaders().set("Authorization", "Bearer " + token);
            } else {
                if (!token.startsWith("Bearer ")) {
                    requestWrapper.getHeaders().set("Authorization", "Bearer " + token);
                } else {
                    log.info("Authorization header already has Bearer prefix");
                }
            }

            // Set Content-Type only if the method supports body (POST, PUT, PATCH)
            if (request.getMethod() != null && (
                    request.getMethod().name().equals("POST") ||
                            request.getMethod().name().equals("PUT") ||
                            request.getMethod().name().equals("PATCH")
            )) {
                requestWrapper.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            }

        } catch (Exception e) {
            log.error("Error handling Authorization header: {}", e.getMessage(), e);
        }

        return execution.execute(requestWrapper, body);
    }
}
