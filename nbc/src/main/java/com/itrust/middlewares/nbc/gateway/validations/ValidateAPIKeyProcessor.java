package com.itrust.middlewares.nbc.gateway.validations;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ValidateAPIKeyProcessor implements Processor {
    /**
     */
    @Override
    public void process(Exchange exchange) throws IllegalArgumentException {
        // Perform request validation (e.g., check headers, params)
        String apiKey = exchange.getIn().getHeader("API-Key", String.class);
        if (apiKey == null || !apiKey.equals("your-api-key")) {
                        throw new IllegalArgumentException("Invalid API Key");
        }
    }
}
