package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.repository.RoomReservationRepository;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelBookingService {

    private final HotelRoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final RoomScraperService roomScraperService;

    @SneakyThrows
    @Transactional
    public void triggerRoomReservationUpdate(LocalDate startDate, LocalDate endDate, Optional<List<Long>> roomsId) {
        boolean isStartDateGreaterThanEndDate = startDate.isAfter(endDate);
        if (isStartDateGreaterThanEndDate) throw new PastDateException("Start date should be less than end date");
        log.info("Getting room reservation details");

        List<Room> rooms = roomsId.map(roomRepository::findAllById).orElseGet(roomRepository::findAll);
        if (rooms.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No rooms available on Database");
        }

        List<RoomReservation> reservations = rooms.stream()
                .flatMap(room -> {
                    var reservationDetails = roomScraperService.getRoomReservationDetails(room.getId(), startDate, endDate);
                    return roomMapper.toReservationEntities(reservationDetails).stream().map(reservation-> reservation.setRoom(room));
                }).toList();
        roomReservationRepository.saveAll(reservations);
    }

    public void triggerRoomUpdate() {
        List<RoomDao> roomDaos = roomScraperService.getAllRooms();
        List<Room> rooms = roomMapper.toRoomEntities(roomDaos).stream()
                .map(newRoom -> {
                    Optional<Room> room =  roomRepository.findById(newRoom.getId());
                    newRoom.setFacilities(room.map(Room::getFacilities).orElse(Collections.emptySet()));
                    newRoom.setImagesUrl(room.map(Room::getImagesUrl).orElse(null));
                    newRoom.setType(room.map(Room::getType).orElse(null));
                    newRoom.setDescription(room.map(Room::getDescription).orElse(null));
                    return newRoom;
                })
                .toList();
        roomRepository.saveAll(rooms);
    }


    public PaginatedRoomDTO getAllRooms(LocalDate checkInDate,
                                       LocalDate checkOutDate,
                                       Integer numberOfRooms,
                                       Integer numberOfAdults,
                                       Optional<Integer> numberOfChildren,
                                       Optional<List<Integer>> childrenAges,
                                       Optional<Set<String>> roomTypes,
                                       Optional<Float> minPrice,
                                       Optional<Float> maxPrice,
                                       Optional<Set<String>> roomFacilities, Pageable pageable) {
        return roomMapper.toPageDto(roomRepository.getRoomAggregated(roomTypes.orElse(Collections.emptySet()),
                minPrice.orElse(null), maxPrice.orElse(null), roomFacilities.orElse(Collections.emptySet()), pageable));
    }
}

