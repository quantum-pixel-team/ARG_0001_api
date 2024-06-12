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