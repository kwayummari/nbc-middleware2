package com.itrust.middlewares.nbc.gateway.mock.cbs.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class CbsRoute extends RouteBuilder {

    final Environment env;

    public CbsRoute(Environment env) {
        this.env = env;
    }

    /**
     */
    @Override
    public void configure() {

        // Route to process inquiry request
        from("direct:cbs-mini-statement")
                .routeId("direct-cbs-mini-statement")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("CBS Processing Mini Statement request: ${body}")
                .to(env.getProperty("mock.server")+"/cbs/statements/mimi-statement?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .log("CBS Processing  Mini Statement response: ${body}")
                .end();

    }

}
