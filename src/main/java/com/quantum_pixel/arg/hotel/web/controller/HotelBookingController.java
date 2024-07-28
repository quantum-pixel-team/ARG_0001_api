package com.quantum_pixel.arg.hotel.web.controller;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import com.quantum_pixel.arg.v1.web.HotelBookingApi;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class HotelBookingController implements HotelBookingApi {
    private final HotelBookingService service;

    @Override
    public ResponseEntity<PaginatedRoomDTO> retrieveAvailableRoomsForDateRange(Integer pageIndex,
                                                                               Integer pageSize,
                                                                               LocalDate checkInDate,
                                                                               LocalDate checkOutDate,
                                                                               Integer numberOfRooms,
                                                                               Integer numberOfAdults,
                                                                               Optional<Integer> numberOfChildren,
                                                                               Optional<List<Integer>> childrenAges,
                                                                               Optional<Set<String>> roomTypes,
                                                                               Optional<Float> minPrice,
                                                                               Optional<Float> maxPrice,
                                                                               Optional<Set<String>> roomFacilities) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        return ResponseEntity.ok(service.getAllRooms(checkInDate, checkOutDate, numberOfRooms,
                numberOfAdults, numberOfChildren, childrenAges, roomTypes, minPrice, maxPrice, roomFacilities, pageable));
    }


    @Override
    public ResponseEntity<Void> triggerRoomReservationUpdate(LocalDate startDate, LocalDate endDate, Optional<List<Long>> roomsId) {
        service.triggerRoomReservationUpdate(startDate, endDate,roomsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> triggerRoomUpdate() {
        service.triggerRoomUpdate();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
