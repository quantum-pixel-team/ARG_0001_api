package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AppITConfig
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReservationUrlBuilderIT extends ConfigTest {

    @Autowired
    private ReservationUrlBuilder sut;

    @Test
    void buildReservationUrl() {

        var expected = "https://app.inn-connect.com/book2/?p=Aragosta+Hotel%26Restaurant#book%7B%22ci%22:%222024-08-29%22,%22co%22:%222024-08-30%22,%22curr%22:%22EUR%22,%22lang%22:%22en%22,%22rooms%22:%5B%7B%22adults%22:1,%22children%22:2,%22rate%22:625727,%22chAges%22:%222,2%22%7D,%7B%22adults%22:1,%22children%22:2,%22rate%22:625727,%22chAges%22:%222,2%22%7D%5D%7D";
        var checkInDate = LocalDateTime.of(LocalDate.of(2024, 8, 29), LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC);
        var checkOutDate = LocalDateTime.of(LocalDate.of(2024, 8, 30), LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC);
        var result = sut.buildReservationUrl(ZoneOffset.UTC, checkInDate, checkOutDate, 2, 1, List.of(2, 2), 625727L);
        assertThat(result).isEqualTo(expected);
    }

}

