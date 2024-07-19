package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {
}
