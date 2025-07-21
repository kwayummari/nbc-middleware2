package com.itrust.middlewares.nbc.gateway.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.auth.dto.gateway.AccessTokenResponse;
import com.itrust.middlewares.nbc.auth.dto.gateway.ClientTokenRequest;
import com.itrust.middlewares.nbc.config.properties.AuthServerProperties;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GatewayAuthRoute extends RouteBuilder {

    final Environment env;

    private final AuthServerProperties authServerProperties;

    public GatewayAuthRoute(Environment env, AuthServerProperties authServerProperties) {
        this.env = env;
        this.authServerProperties = authServerProperties;
    }

    @Override
    public void configure() {

        // Register client
        from("direct:clients-register")
                .routeId("direct-clients-register")
                .outputType(GenericRestResponse.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .log("api request: ${body}")
                .to(authServerProperties.getIssuerUri()+"/v1/auth/clients/register?bridgeEndpoint=true")
                .log("api response: ${body}")
                .unmarshal().json(JsonLibrary.Jackson, GenericRestResponse.class)  // Explicitly convert JSON response
                .setBody(exchange -> exchange.getIn().getBody(GenericRestResponse.class))
                .end();

        // Generate token
        from("direct:clients-generate-token")
                .routeId("direct-clients-generate-token")
                .outputType(GenericRestResponse.class)
                .marshal().json()
                .convertBodyTo(InputStream.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
                .process(exchange -> {
                    ObjectMapper mapper = new ObjectMapper();
                    String bodyInput = exchange.getIn().getBody(String.class);
                    ClientTokenRequest tokenRequest = mapper.readValue(bodyInput, ClientTokenRequest.class);
                    Map<String, Object> formData = new HashMap<>();
                    formData.put("grant_type", tokenRequest.getGrantType());
                    formData.put("scope", tokenRequest.getScope());

                    // Convert map to URL-encoded string
                    String body = formData.entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining("&"));

                    exchange.getIn().setHeader(Exchange.CONTENT_TYPE, "application/x-www-form-urlencoded");
                    exchange.getIn().setBody(body);
                })
                .log("api request: ${body}")
                .to(authServerProperties.getIssuerUri()+"/oauth2/token?bridgeEndpoint=true")
                .log("api response: ${body}")
                .process(exchange -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = exchange.getIn().getBody(String.class);
                    AccessTokenResponse accessTokenResponse = objectMapper.readValue(response, AccessTokenResponse.class);
                    GenericRestResponse<AccessTokenResponse> genericRestResponse = new GenericRestResponse<>(); // Fixed generic type
                    genericRestResponse.setTimestamp(DateTimeUtil.dateTime());

                    if (accessTokenResponse.getAccessToken() == null) {
                        genericRestResponse.setStatusCode(ResponseCode.VALIDATION_FAILED.getCode());
                        genericRestResponse.setMessage("Invalid credentials");
                    } else {
                        genericRestResponse.setStatusCode(ResponseCode.SUCCESS.getCode());
                        genericRestResponse.setMessage("Token generated successfully");
                        genericRestResponse.setData(accessTokenResponse); // Add the response data
                    }

                    exchange.getIn().setBody(genericRestResponse);
                })
//                .unmarshal().json(JsonLibrary.Jackson, GenericRestResponse.class)  // Explicitly convert JSON response
                .setBody(exchange -> exchange.getIn().getBody(GenericRestResponse.class))
                .end();

        // @formatter:on
    }

}
