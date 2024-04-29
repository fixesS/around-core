package com.around.aroundcore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Around-core API",
                description = "API of Around backend", version = "1.0",
                contact = @Contact(
                        name = "Eduard Gorshkov",
                        email = "eduard.gorshkov.2020@gmail.com",
                        url = "t.me/fx_yes"
                )
        )

)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

}
