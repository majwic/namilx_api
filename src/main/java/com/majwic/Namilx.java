package com.majwic;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Namilx",
        version = "Demo",
        description = "This is the API documentation for Namilx."
    )
)
public class Namilx {

    public static void main(String... args) {
        SpringApplication.run(Namilx.class, args);
    }
}
