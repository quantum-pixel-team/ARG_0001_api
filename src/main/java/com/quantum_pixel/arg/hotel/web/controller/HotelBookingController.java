package com.quantum_pixel.arg.hotel.web.controller;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import com.quantum_pixel.arg.v1.web.HotelBookingApi;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomAvailabilityDTO;
import com.quantum_pixel.arg.v1.web.model.RoomFiltersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class HotelBookingController implements HotelBookingApi {
    private final HotelBookingService service;

    @Override
    public ResponseEntity<List<RoomAvailabilityDTO>> getRoomAvailability(Long roomId,
                                                                         OffsetDateTime startDate,
                                                                         OffsetDateTime endDate) {
        return ResponseEntity.ok(service.getRoomAvailability(roomId, startDate, endDate));
    }

    @Override
    public ResponseEntity<PaginatedRoomDTO> retrieveAvailableRoomsForDateRange(ZoneOffset zoneOffset, RoomFiltersDTO roomFiltersDTO) {
        return ResponseEntity.ok(service.getPaginatedRooms(zoneOffset, roomFiltersDTO));
    }


    @Override
    public ResponseEntity<Void> triggerRoomReservationUpdate(OffsetDateTime startDate,
                                                             OffsetDateTime endDate, Optional<List<Long>> roomsId) {
        service.triggerRoomReservationUpdate(startDate, endDate, roomsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> triggerRoomUpdate() {
        service.triggerRoomUpdate();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
