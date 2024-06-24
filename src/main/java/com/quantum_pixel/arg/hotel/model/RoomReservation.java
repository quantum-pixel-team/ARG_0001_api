package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "room_reservations" )
public class RoomReservation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "room_name")
    private String roomName;
    @Column(name = "reservation_date")
    private LocalDate reservationDate;
    private Boolean sold;
    private Integer currentPrice;
    private Integer roomPrice;

    private String checkoutUrl;
}
