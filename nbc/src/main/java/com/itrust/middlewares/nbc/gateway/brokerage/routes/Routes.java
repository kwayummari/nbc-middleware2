package com.itrust.middlewares.nbc.gateway.brokerage.routes;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class Routes extends RouteBuilder {

    final Environment env;

    public Routes(Environment env) {
        this.env = env;
    }

    /**
     */
    @Override
    public void configure() {

        onException(HttpOperationFailedException.class)
                .handled(true)
                .process(exchange -> {
                    // Extract the exception
                    HttpOperationFailedException exception =
                            exchange.getProperty(Exchange.EXCEPTION_CAUGHT, HttpOperationFailedException.class);

                    // Get HTTP status code
                    int statusCode = exception.getStatusCode();

                    // Get response body
                    String responseBody = exception.getResponseBody();

                    // Log the error details
                    System.out.println("HTTP Status Code: " + statusCode);
                    System.out.println("Response Body: " + responseBody);

                })
                .log("Handled HttpOperationFailedException: ${exception.message}");

        from("direct:brokerage-create-account")
                .routeId("direct-brokerage-create-account")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Cache-Control", constant("no-cache, no-store, must-revalidate"))
                .setHeader("Pragma", constant("no-cache"))
                .setHeader("Expires", constant("0"))
                .log("Brokerage Request: ${body}")
                .to(env.getProperty("brokerage.base.url")+"/nbc/account-open?bridgeEndpoint=true")
                .log("Brokerage Response: ${body}")
                .setBody(exchange -> exchange.getIn().getBody())
                .end();

    }

}
