package com.itrust.middlewares.nbc.gateway.mock.tips.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class TipsRoute extends RouteBuilder {

    final Environment env;

    public TipsRoute(Environment env) {
        this.env = env;
    }

    /**
     */
    @Override
    public void configure() {

        // Route to process inquiry request
        from("direct:tips-inquiry")
                .routeId("direct-tips-inquiry")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("TIPS Processing inquiry request: ${body}")
                .to(env.getProperty("mock.server")+"/tips/lookup?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .log("TIPS Processing inquiry response: ${body}")
                .end();

        // Route to process confirm request
        from("direct:tips-confirm")
                .routeId("direct-tips-confirm")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("TIPS Processing confirm request: ${body}")
                .to(env.getProperty("mock.server")+"/tips/confirm?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .log("TIPS Processing confirm response: ${body}")
                .end();

        // Route to process transfer request
        from("direct:tips-transfer")
                .routeId("direct-tips-transfer")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("TIPS Processing transfer request: ${body}")
                .to(env.getProperty("mock.server")+"/tips/transfer?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .log("TIPS Processing transfer response: ${body}")
                .end();

        // Route to process transfer request
        from("direct:tips-service-providers")
                .routeId("direct-tips-service-providers")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .log("TIPS Processing service providers request: ${body}")
                .to(env.getProperty("mock.server")+"/tips/service-providers?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .log("TIPS Processing service providers response: ${body}")
                .end();

    }

}
