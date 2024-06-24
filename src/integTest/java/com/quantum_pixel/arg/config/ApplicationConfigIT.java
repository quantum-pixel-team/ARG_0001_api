package com.quantum_pixel.arg.config;

import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AppITConfig
class ApplicationConfigIT extends ConfigTest {

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

    @Override
    protected List<String> getTableToTruncate() {
        return List.of();
    }
}