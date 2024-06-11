package com.quantum_pixel.arg.whatsapp.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class WhatsAppConfig {
    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;
    @Value("${whatsapp.api.to}")
    private String to;

}