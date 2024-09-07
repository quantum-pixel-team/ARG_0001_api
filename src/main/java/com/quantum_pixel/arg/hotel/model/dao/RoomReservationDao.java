package com.quantum_pixel.arg.hotel.model.dao;

import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record RoomReservationDao(Long roomId,
                                 OffsetDateTime date,
                                 Double currentPrice,
                                 Integer available,
                                 Integer minimumNights) {
}
