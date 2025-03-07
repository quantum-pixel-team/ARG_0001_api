package com.quantum_pixel.arg.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.time.ZoneOffset;
@Configuration
public class ApplicationConfig {


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Clock getClock() {
        return Clock.system(ZoneOffset.UTC);
    }
}
