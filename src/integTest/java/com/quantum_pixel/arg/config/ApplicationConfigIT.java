package com.quantum_pixel.arg.config;

import com.quantum_pixel.arg.AppITConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AppITConfig
class ApplicationConfigIT {

    @Autowired
    ApplicationConfig applicationConfig;

    @Test
    void getRestTemplate() {

        // when
        RestTemplate restTemplate = applicationConfig.getRestTemplate();
        // then
        assertThat(restTemplate)
                .isNotNull();
    }
}