package com.quantum_pixel.arg.hotel.model.dao;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RoomReservationDao(Long roomId,
                                 LocalDate date,
                                 Double currentPrice,
                                 Integer available,
                                 Integer minimumNights) {
}
