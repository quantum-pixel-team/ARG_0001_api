package com.quantum_pixel.arg.hotel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class RoomPrototype {
    private String name;
    private Integer price;
    private List<RoomReservation> roomReservations;

    public RoomPrototype setRoomReservations(List<RoomReservation> roomReservations) {
        this.roomReservations = roomReservations;
        return this;
    }
}
