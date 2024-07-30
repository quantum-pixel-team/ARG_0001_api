package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.repository.RoomReservationRepository;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

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
    private final ReservationUrlBuilder reservationUrlBuilder;

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
                    return roomMapper.toReservationEntities(reservationDetails).stream().map(reservation -> reservation.setRoom(room));
                }).toList();
        roomReservationRepository.saveAll(reservations);
    }

    public void triggerRoomUpdate() {
        List<RoomDao> roomDaos = roomScraperService.getAllRooms();
        List<Room> rooms = roomMapper.toRoomEntities(roomDaos).stream()
                .map(newRoom -> {
                    Optional<Room> room = roomRepository.findById(newRoom.getId());
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
                                        Optional<List<Integer>> childrenAges,
                                        Optional<Set<String>> roomTypes,
                                        Optional<Double> minPrice,
                                        Optional<Double> maxPrice,
                                        Optional<Set<String>> roomFacilities, Pageable pageable) {

        if (checkInDate.isBefore(LocalDate.now()) ||
                checkOutDate.isBefore(checkInDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in should not be on past, and check out date should be after check in date!");
        }
        if(minPrice.orElse(0d) > maxPrice.orElse(0d))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Min price should be less than max price!");

        var numberOfGuests = numberOfAdults + childrenAges.map(children -> (int) children.stream().filter(age -> age > 6).count()).orElse(0);

        var rooms = roomRepository.getRoomAggregated(
                checkInDate,
                checkOutDate,
                numberOfGuests,
                numberOfRooms,
                roomTypes.orElse(Collections.emptySet()).toArray(new String[0]),
                minPrice.orElse(null),
                maxPrice.orElse(null),
                roomFacilities.orElse(Collections.emptySet()).toArray(new String[0]),
                pageable);
        if (rooms.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rooms available on Database");

        PaginatedRoomDTO pageDto = roomMapper.toPageDto(rooms);
        List<RoomDTO> updatedContent = pageDto.getContent().stream()
                .map(el -> el.bookNowUrl(reservationUrlBuilder.buildReservationUrl(checkInDate, checkOutDate, numberOfRooms, numberOfAdults, childrenAges, el.getId())))
                .toList();
        return pageDto.content(updatedContent);

    }


}
