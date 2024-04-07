package com.muhammed.springsecurity.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring Security JWT API",
                version = "1.0",
                description = "API documentation for a Spring Security JWT implementation project",
                contact = @Contact(
                        name = "Muhammed Emin Karaaslan",
                        email = "mhmmdkaraaslan@gmail.com"
                )
        ),
        security = {
                @SecurityRequirement(
                        name = "JWT Authentication"
                )
        }
)
@SecurityScheme(
        name = "JWT Authentication",
        description = "JWT authentication using bearer token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
