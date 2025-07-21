package com.itrust.middlewares.nbc.gateway.routes;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class GepgRoute extends RouteBuilder {

    /**
     */
    @Override
    public void configure()  {


        // Handle errors globally
        onException(IllegalArgumentException.class)
                .handled(true)
                .setBody(simple("Unauthorized access"))
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("status", constant(401)); // Unauthorized

        // Route to validate the incoming request
        from("direct:validateRequest")
                .routeId("validateRequest")
                .process(exchange -> {
                    // Perform request validation (e.g., check headers, params)
                    String apiKey = exchange.getIn().getHeader("API-Key", String.class);
                    if (apiKey == null || !apiKey.equals("your-api-key")) {
//                        throw new IllegalArgumentException("Invalid API Key");
                    }

                    // Token validation (JWT)
                    String token = exchange.getIn().getHeader("Authorization", String.class);
                    if (token == null || !isValidToken(token)) {
//                        throw new IllegalArgumentException("Invalid or missing token");
                    }
                })
                .to("direct:forwardRequest"); // Forward to external API

        // Route to forward request to external API
        from("direct:forwardRequest")
                .routeId("forwardRequest")
                .setHeader("Authorization", simple("Bearer ${header.Authorization}")) // Forward token
                .log("Incoming request: ${body}")
                .choice()
                    // Route to API1 if header `apiType` is "api1"
                    .when(header("apiType").isEqualTo("brokerage"))
                    .to("https://pos.hollywood.co.tz")
                    // Default route if no match
                    .otherwise()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setBody(constant("Invalid apiType header"))
                .log("Response: ${body}")
                .end();

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
