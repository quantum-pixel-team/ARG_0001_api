package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomReservationRepository extends CrudRepository<RoomReservation,Integer> {
}
