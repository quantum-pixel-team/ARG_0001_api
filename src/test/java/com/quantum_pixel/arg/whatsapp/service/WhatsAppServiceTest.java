package com.quantum_pixel.arg.whatsapp.service;

import com.quantum_pixel.arg.v1.web.model.RestaurantReservationRequestDTO;
import com.quantum_pixel.arg.whatsapp.config.WhatsAppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WhatsAppServiceTest {

    @Mock
    private WhatsAppConfig config;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WhatsAppService whatsAppService;


    @Test
    void sendRestaurantReservationDetails() {
        // Mock configurations
        when(config.getApiUrl()).thenReturn("https://api.whatsapp.com");
        when(config.getApiToken()).thenReturn("dummyToken");
        when(config.getPhoneNumberId()).thenReturn("987654321");

        // Create a sample reservation request DTO
        RestaurantReservationRequestDTO reservationRequestDTO = RestaurantReservationRequestDTO.builder()
                .languageCode(Optional.of(RestaurantReservationRequestDTO.LanguageCodeEnum.EN))
                .name("John Doe")
                .phoneNumber("0987654321")
                .to("355676923049")
                .guests(4)
                .date(OffsetDateTime.of(LocalDate.parse("2023-07-01").atStartOfDay(), ZoneOffset.UTC))
                .time(OffsetTime.of(LocalTime.parse("19:00"), ZoneOffset.UTC))
                .message(Optional.of("Please arrange a corner table"))
                .build();

        // Capture the HttpEntity
        ArgumentCaptor<HttpEntity<String>> captor = ArgumentCaptor.forClass(HttpEntity.class);

        // Execute the method
        whatsAppService.sendRestaurantReservationDetails(reservationRequestDTO);

        // Verify interactions and capture the HttpEntity
        verify(restTemplate, times(1)).postForObject(anyString(), captor.capture(), eq(String.class));

        HttpEntity<String> request = captor.getValue();

        // Assert headers
        HttpHeaders headers = request.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("Bearer dummyToken", headers.getFirst(HttpHeaders.AUTHORIZATION));

        // Assert body

        String requestBody = request.getBody();
        assertThat(requestBody)
                .contains(reservationRequestDTO.getName())
                .contains(reservationRequestDTO.getPhoneNumber())
                .contains(reservationRequestDTO.getDate().toString())
                .contains(reservationRequestDTO.getMessage().orElseThrow());
    }
}
