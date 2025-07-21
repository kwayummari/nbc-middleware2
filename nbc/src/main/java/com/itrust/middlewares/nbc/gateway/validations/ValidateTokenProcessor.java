package com.itrust.middlewares.nbc.gateway.validations;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ValidateTokenProcessor implements Processor {
    /**
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // Token validation (JWT)
        String token = exchange.getIn().getHeader("Authorization", String.class);
        if (token == null || !isValidToken(token)) {
                        throw new IllegalArgumentException("Invalid or missing token");
        }
    }


    // Simple JWT validation logic
    private boolean isValidToken(String token) {
        try {
            // You can implement actual token validation logic here (e.g., parsing JWT token)
            // For now, we just check if the token starts with "Bearer "
            return token != null && token.startsWith("Bearer ");
        } catch (Exception e) {
            return false;
        }
    }
}
