package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.model.app_in_connect.Rate;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomReservationHttpServiceTest {

    @Mock
    private RestTemplate restTemplate;


    @InjectMocks
    private RoomReservationHttpService sut;
    String mockResponseBody = "//OK[1,[\"[{\\\"rateId\\\":1,\\\"dates\\\":[{\\\"date\\\":\\\"2024-07-19T00:00:00.000\\\",\\\"available\\\":false,\\\"avail\\\":0,\\\"minStay\\\":1,\\\"guests\\\":1,\\\"capacity\\\":2,\\\"rate\\\":\\\"90.00\\\"}]}]\"],0,7]";

    @Test
    void testFetchRoomRates() {
        // Arrange
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2024, 7, 19);
        LocalDate endDate = LocalDate.of(2024, 7, 20);

        ResponseEntity<String> responseEntity = ResponseEntity.ok(mockResponseBody);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // Act
        Rate rate = sut.fetchRoomRates(roomId, startDate, endDate);

        // Assert
        assertNotNull(rate);
        assertEquals(1L, rate.getRateId());
        assertEquals(LocalDate.of(2024, 7, 19).atStartOfDay(), rate.getDates().getFirst().getDate());
        assertEquals("90.00", rate.getDates().getFirst().getRate());
    }

    @Test
    void testFetchRoomRates_badResponse() {
        // Arrange
        Long roomId = 1L;
        LocalDate startDate = LocalDate.of(2024, 7, 19);
        LocalDate endDate = LocalDate.of(2024, 7, 20);

        ResponseEntity<String> responseEntity = ResponseEntity.ok(null);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        // Act
        assertThrows(BadRequestException.class, () -> sut.fetchRoomRates(roomId, startDate, endDate));

    }
}