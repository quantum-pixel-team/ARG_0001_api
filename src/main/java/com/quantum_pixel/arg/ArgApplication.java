package com.quantum_pixel.arg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.MDC;

@SpringBootApplication

public class ArgApplication {

    public static void main(String[] args) {
        MDC.put("component", "API");
        MDC.put("app_id", "ARG_0001");

        SpringApplication.run(ArgApplication.class, args);
    }

}
