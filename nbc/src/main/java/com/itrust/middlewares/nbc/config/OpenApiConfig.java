package com.itrust.middlewares.nbc.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "NBC Middleware",
        version = "v1.0.0",
        description = "A Central entry point that handles all incoming requests from clients (web, mobile, etc.) and routes them to the appropriate backend microservices"
    )
)
@Configuration
public class OpenApiConfig {
}
