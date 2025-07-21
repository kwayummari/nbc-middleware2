package com.itrust.middlewares.nbc.gateway;


import com.itrust.middlewares.nbc.gateway.validations.ValidateAPIKeyProcessor;
import com.itrust.middlewares.nbc.gateway.validations.ValidateTokenProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ApiGateway extends RouteBuilder {

    /**
     */
    @Override
    public void configure() {


        // Handle errors globally
        onException(IllegalArgumentException.class)
                .handled(true)
                .setBody(simple("Unauthorized access"))
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("status", constant(401)); // Unauthorized

        // Route to validate the incoming request
        from("direct:validateRequest")
                .routeId("validateRequest")
                // Validate API Key
                .process(new ValidateAPIKeyProcessor())
                // Validate Token
                .process(new ValidateTokenProcessor())
                .to("direct:forwardRequest"); // Forward to external API

        // Route to forward request to external API
        from("direct:forwardRequest")
                .routeId("forwardRequest")
                .setHeader("Authorization", simple("Bearer ${header.Authorization}")) // Forward token
                .log("Incoming request: ${body}")
                .choice()
                    // Route to API1 if header `apiType` is "api1"
                    .when(header("apiType").isEqualTo("brokerage"))
                    .to("http://192.168.1.162:40400")
                    // Default route if no match
                    .otherwise()
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setBody(constant("Invalid apiType header"))
                .log("Response: ${body}")
                .end();

    }


}
