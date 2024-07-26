package com.quantum_pixel.arg.hotel.repository;

import com.quantum_pixel.arg.hotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r JOIN r.roomReservations rr WHERE rr.id.date BETWEEN :startDate AND :endDate")
    List<Room> findAvailableRooms(LocalDate startDate, LocalDate endDate);
}
