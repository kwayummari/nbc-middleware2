package com.itrust.middlewares.nbc.gateway.mock.kyc.routes;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
public class OTPRoutes extends RouteBuilder {

    final Environment env;

    public OTPRoutes(Environment env) {
        this.env = env;
    }

    /**
     */
    @Override
    public void configure() {

        from("direct:otp-send")
                .routeId("direct-otp-send")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(env.getProperty("mock.server")+"/otp/send?bridgeEndpoint=true")
                .log("Response: ${body}")
                .setBody(exchange -> exchange.getIn().getBody())
                .end();

        from("direct:otp-verify")
                .routeId("direct-otp-verify")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(env.getProperty("mock.server")+"/otp/verify?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .end();

    }

}
