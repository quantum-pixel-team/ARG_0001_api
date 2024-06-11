package com.quantum_pixel.arg.whatsapp.service;

import com.quantum_pixel.arg.whatsapp.config.WhatsAppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final WhatsAppConfig config;

    public void sendMessage(String phoneNumber, String message) {
        RestTemplate restTemplate = new RestTemplate();

        String url = config.getApiUrl();
        String token = config.getApiToken();
        String to = config.getTo();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        String jsonBody = String.format("""
                {
                  "messaging_product": "whatsapp",
                  "to": "%s",
                  "type": "template",
                  "template": {
                    "name": "hello_world",
                    "language": {
                      "code": "en_US"
                    }
                  }
                }
                """,to);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        restTemplate.postForObject(url, request, String.class);
    }
}