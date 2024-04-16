package com.quantum_pixel.arg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class ArgApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArgApplication.class, args);
    }

}
