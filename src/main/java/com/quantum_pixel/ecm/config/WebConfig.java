package com.quantum_pixel.ecm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = environment.getProperty("allowed-origins", String[].class);

        String[] allowedOrigins = origins != null ?
                origins : new String[]{"*"};
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600)
                .allowCredentials(false);
    }
}