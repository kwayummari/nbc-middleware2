package com.itrust.middlewares.nbc.gateway.mock.kyc.routes;


import com.itrust.middlewares.nbc.gateway.mock.kyc.processors.KYCResponseProcessor;
import com.itrust.middlewares.nbc.kycValidation.services.OnboardingService;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.io.InputStream;

@Component
public class KYCRoutes extends RouteBuilder {

    /**
     * The Environment instance used to access application properties.
     */
    final Environment env;

    /**
     * The OnboardingService instance used for processing KYC responses.
     */
    private final OnboardingService onboardingService;

    /**
     * Constructor for KYCRoutes.
     *
     * @param env The Environment instance to use for accessing application properties.
     * @param onboardingService The OnboardingService instance to use for processing.
     */
    public KYCRoutes(Environment env, OnboardingService onboardingService) {
        this.env = env;
        this.onboardingService = onboardingService;
    }

  /**
     * Configure the Camel routes for KYC processing.
     *
   */
    @Override
    public void configure() {

        // Route to NIDA Questionnaire End Point
        from("direct:validation-questionnaire")
                .routeId("direct-questionnaire-validation")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(env.getProperty("mock.server")+"/onboarding/kyc-validation/questionnaire?bridgeEndpoint=true")
                .process(new KYCResponseProcessor(onboardingService))
                .setBody(exchange -> exchange.getIn().getBody())
                .log("mock response: ${body}")
                .end();

        // Route to NIDA Service Biometric End Point
        from("direct:validation-biometric")
                .log("mock response: ${body}")
                .routeId("direct-validation-biometric")
                .outputType(String.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to(env.getProperty("mock.server")+"/onboarding/kyc-validation/biometric?bridgeEndpoint=true")
                .process(new KYCResponseProcessor(onboardingService))
                .setBody(exchange -> exchange.getIn().getBody())
                .log("mock response: ${body}")
                .end();
    }

}
