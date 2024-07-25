package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelBookingService {

    private final HotelRoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final RoomScraperService roomScraperService;

    @SneakyThrows
    @Transactional
    public void triggerRoomReservationUpdate(LocalDate startDate, LocalDate endDate) {
        boolean isStartDateGreaterThanEndDate = startDate.isAfter(endDate);
        if (isStartDateGreaterThanEndDate) throw new PastDateException("Start date should be less than end date");
        log.info("Getting room reservation details");
        List<RoomDao> roomDaos = roomScraperService.getRoom(startDate, endDate);
        List<Room> rooms = roomMapper.toEntity(roomDaos)
                .stream().peek(room -> {
                    var reservationList = room.getRoomReservations().stream().map(res -> res.setRoom(room)).collect(Collectors.toSet());
                    room.setRoomReservations(reservationList);
                }).toList();
        roomRepository.saveAll(rooms);
    }


    public List<RoomDTO> retrieveAvailableRoomsForDateRange(LocalDate startDate, LocalDate endDate) {
        return roomMapper.toDTOList(roomRepository.findAvailableRooms(startDate, endDate));
    }
}

