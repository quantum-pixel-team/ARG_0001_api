package com.quantum_pixel.arg.hotel.model.dao;

import lombok.Builder;

@Builder
public record RoomDao(Long id, String name, Double price, Integer capacity, Integer rateAppliesTo) {
}
