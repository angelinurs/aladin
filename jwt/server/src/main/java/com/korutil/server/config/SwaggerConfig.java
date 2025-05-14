package com.korutil.server.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class SwaggerConfig {

    private static final String SWAGGER_CONTROLLER = "com.korutil.server.controller";

//    @Bean
//    public GroupedOpenApi widgetAPI() {
//        return GroupedOpenApi.builder()
//                .group("widget-api")
//                .packagesToScan(SWAGGER_CONTROLLER)
//                .build();
//    }

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    @Profile("dev")
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("API Server with JWT")
                    .description("API Service Server")
                    .version("v1.0"))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme(BEARER_TOKEN_PREFIX)
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                        ));
    }

}