package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.repository.RoomReservationRepository;
import com.quantum_pixel.arg.hotel.specification.RoomReservationSearch;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomAvailabilityDTO;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomFiltersDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.quantum_pixel.arg.v1.web.model.RoomFiltersDTO.SortEnum.ASC;

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
                    newRoom.setImagesUrl(room.map(Room::getImagesUrl).orElse(null));
                    newRoom.setFacilities(room.map(Room::getFacilities).orElse(Collections.emptySet()));
                    return newRoom;
                })
                .toList();
        roomRepository.saveAll(rooms);
    }

    public PaginatedRoomDTO getPaginatedRooms(RoomFiltersDTO filtersDTO) {
        var numberOfGuests = filtersDTO.getNumberOfAdults() + Optional.ofNullable(filtersDTO.getChildrenAges())
                .map(children -> (int) children.stream().filter(age -> age > 6).count())
                .orElse(0);

        Sort sort = Sort.by(
                filtersDTO.getSort() == ASC ? Sort.Order.asc("total_price") : Sort.Order.desc("total_price"),
                Sort.Order.desc("available_rooms"),
                Sort.Order.desc("total_capacity")
        );
        LocalDate checkInDate = filtersDTO.getCheckInDate();
        LocalDate checkOutDate = filtersDTO.getCheckOutDate();

        if (checkInDate.isBefore(LocalDate.now()) ||
                checkOutDate.isBefore(checkInDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in should not be on past, and check out date should be after check in date!");
        }
        if (checkInDate.isEqual(checkOutDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in should not be same with checkout!");
        }
        if (filtersDTO.getMinPrice().orElse(0d) > filtersDTO.getMaxPrice().orElse(Double.MAX_VALUE))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Min price should be less than max price!");

        String[] roomTypes = Optional.ofNullable(filtersDTO.getRoomTypes())
                .map(el -> el.stream()
                        .map(String::toLowerCase)
                        .map(type -> "%" + type + "%")
                        .toArray(String[]::new))
                .orElse(new String[0]);


        Pageable pageable = PageRequest.of(filtersDTO.getPageIndex(), filtersDTO.getPageSize(), sort);

        String language = filtersDTO.getLanguage();
        var rooms = roomRepository.getRoomAggregated(
                checkInDate,
                checkOutDate,
                language,
                numberOfGuests,
                filtersDTO.getNumberOfRooms(),
                roomTypes,
                filtersDTO.getMinPrice().orElse(null),
                filtersDTO.getMaxPrice().orElse(null),
                Optional.ofNullable(filtersDTO.getRoomFacilities()).orElse(Collections.emptySet()).toArray(new String[0]),
                filtersDTO.getAvailable(),
                pageable);


        if (rooms.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rooms available on Database");

        PaginatedRoomDTO pageDto = roomMapper.toPageDto(rooms);
        List<RoomDTO> updatedContent = pageDto.getContent().stream()
                .map(el -> el.bookNowUrl(reservationUrlBuilder.buildReservationUrl(checkInDate,
                        checkOutDate, filtersDTO.getNumberOfRooms(),
                        filtersDTO.getNumberOfAdults(), filtersDTO.getChildrenAges(), el.getId())))
                .toList();
        return pageDto.content(updatedContent);
    }


    public List<RoomAvailabilityDTO> getRoomAvailability(Long roomId, LocalDate startDate, LocalDate endDate) {

        var rooms = roomReservationRepository.findAll(new RoomReservationSearch(roomId, startDate, endDate).getSpecification());
        return rooms.stream().map(roomMapper::toRoomAvailability).toList();
    }
}
