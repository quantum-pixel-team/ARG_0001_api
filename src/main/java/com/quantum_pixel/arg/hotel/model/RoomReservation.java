package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_reservation")
public class RoomReservation {
    @EmbeddedId
    private RoomReservationId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "current_price")
    private Float currentPrice;

    @Column(name = "available")
    private Integer available;

    @Column(name = "minimum_nights")
    private Integer minimumNights;

    public RoomReservation setRoom(Room room) {
        this.room = room;
        return this;
    }
}
