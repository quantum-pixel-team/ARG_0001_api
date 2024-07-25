package com.quantum_pixel.arg.hotel.web.controller;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import com.quantum_pixel.arg.v1.web.HotelBookingApi;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HotelBookingController implements HotelBookingApi {
    private final HotelBookingService service;

    @Override
    public ResponseEntity<List<RoomDTO>> retrieveAvailableRoomsForDateRange(LocalDate startDate, LocalDate endDate) {
        return ResponseEntity.ok(service.retrieveAvailableRoomsForDateRange(startDate, endDate));
    }

    @Override
    public ResponseEntity<Void> triggerRoomReservationUpdate(LocalDate startDate, LocalDate endDate) {
        service.triggerRoomReservationUpdate(startDate, endDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
