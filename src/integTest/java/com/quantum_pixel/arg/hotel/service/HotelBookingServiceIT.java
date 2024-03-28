package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.AppITConfig;
import com.quantum_pixel.arg.ConfigTest;
import com.quantum_pixel.arg.v1.web.model.RoomReservationDTO;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AppITConfig
class HotelBookingServiceIT extends ConfigTest {


    @Autowired
    private HotelBookingService sut;

    @Test
    void getRoomReservationsForNext14Days() {
        // given
        var startDate = Optional.of(LocalDate.now());
        Optional<LocalDate> endDate = Optional.empty();
        // when
        var result = sut.getRoomReservationsForGivenRangeOfDate(startDate, endDate);

        // then
        assertThat(result.get(0).getRoomReservations())
                .hasSize(14)
                .extracting(RoomReservationDTO::getDate)
                .first()
                .isEqualTo(LocalDate.now());

    }

    @Test
    void getRoomReservationsForGivenRangeOfDateEmptyStartDate() {
        // given
        Optional<LocalDate> startDate = Optional.empty();
        Optional<LocalDate> endDate = Optional.empty();
        // when
        var result = sut.getRoomReservationsForGivenRangeOfDate(startDate, endDate);

        // then
        assertThat(result.get(0).getRoomReservations())
                .hasSize(14)
                .extracting(RoomReservationDTO::getDate)
                .first()
                .isEqualTo(LocalDate.now());

    }

    @Test
    void getRoomReservationsForGivenRangeOfDate() {
        // given
        Optional<LocalDate> startDate = Optional.of(LocalDate.now());
        Optional<LocalDate> endDate = Optional.of(LocalDate.now().plusDays(15));
        // when
        var result = sut.getRoomReservationsForGivenRangeOfDate(startDate, endDate);

        // then
        assertThat(result.get(0).getRoomReservations())
                .hasSize(16)
                .extracting(RoomReservationDTO::getDate)
                .first()
                .isEqualTo(LocalDate.now());
        assertThat(result.get(0).getRoomReservations())
                .extracting(RoomReservationDTO::getDate)
                .last()
                .isEqualTo(endDate.get());
    }

    @Test
    void getRoomReservationsForGivenRangeOfDateWrongParam() {
        // given
        Optional<LocalDate> startDate = Optional.of(LocalDate.now());
        Optional<LocalDate> endDate = Optional.of(LocalDate.now().minusDays(1));
        // when
        assertThrows(BadRequestException.class, () -> sut.getRoomReservationsForGivenRangeOfDate(startDate, endDate));

    }

    @Override
    protected List<String> getTableToTruncate() {
        return Collections.emptyList();
    }
}