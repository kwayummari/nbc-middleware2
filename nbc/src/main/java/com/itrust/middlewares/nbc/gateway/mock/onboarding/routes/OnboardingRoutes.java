package com.itrust.middlewares.nbc.gateway.mock.onboarding.routes;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
public class OnboardingRoutes extends RouteBuilder {

    final Environment env;

    public OnboardingRoutes(Environment env) {
        this.env = env;
    }

    /**
     */
    @Override
    public void configure() {

        from("direct:onboarding-create-account")
                .routeId("direct-onboarding-create-account")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("request log ${body}")
                .to(env.getProperty("mock.server")+"/onboarding/create-account?bridgeEndpoint=true")
                .log("response log ${body}")
                .setBody(exchange -> exchange.getIn().getBody())
                .end();

        from("direct:onboarding-create-account-retry")
                .routeId("direct-onboarding-create-account-retry")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(env.getProperty("mock.server")+"/onboarding/create-account-retry?bridgeEndpoint=true")
                .setBody(exchange -> exchange.getIn().getBody())
                .end();

    }

}
