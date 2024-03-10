package com.quantum_pixel.arg.hotel.web.controller;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import com.quantum_pixel.arg.v1.web.HotelBookingApi;
import com.quantum_pixel.arg.v1.web.model.RoomPrototypeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HotelBookingController implements HotelBookingApi {
    private final HotelBookingService service;
    @Override
    public ResponseEntity<List<RoomPrototypeDTO>> getRoomsForGivenRangeOfDate(Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
      return  ResponseEntity.ok(service.getRoomReservationsForGivenRangeOfDate(startDate, endDate));
    }
}
