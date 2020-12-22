package br.com.grillo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@Profile("!heroku")
@OpenAPIDefinition(
  info = @Info(
  title = "Grillo API",
  description = "" +
    "Lorem ipsum dolor ...",
  contact = @Contact(
    name = "Lomonaco", 
    email = "fernando_lomonaco@outlook.com"
  )),
  servers = @Server(url = "http://localhost:8080")
)
@SecurityScheme(
  name = "api",   
  scheme = "bearer",
  bearerFormat = "JWT",
  type = SecuritySchemeType.HTTP,
  in = SecuritySchemeIn.HEADER)
public class OpenApiConfigration {

    
}