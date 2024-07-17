package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
}
