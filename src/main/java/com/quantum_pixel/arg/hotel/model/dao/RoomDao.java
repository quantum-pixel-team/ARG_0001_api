package com.quantum_pixel.arg.hotel.model.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class RoomDao {
    private Long id;
    private String name;
    private Double price;
    private Integer capacity;
    private Integer rateAppliesTo;
    private List<RoomReservationDao> roomReservations;

}
