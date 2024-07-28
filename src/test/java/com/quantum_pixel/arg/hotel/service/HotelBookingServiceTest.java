package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.RoomReservationId;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.model.dao.RoomReservationDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.repository.RoomReservationRepository;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelBookingServiceTest {

    @Mock
    private RoomScraperService roomScraperService;

    @Mock
    private HotelRoomMapper roomMapper;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private HotelBookingService sut;
    @Mock
    private RoomReservationRepository roomReservationRepository;

    private Room room;
    RoomReservationDao roomReservationDao;

    @BeforeEach
    void setUp() {
        roomReservationDao = RoomReservationDao.builder()
                .roomId(1L)
                .date(LocalDate.now())
                .currentPrice(100.0)
                .available(1)
                .minimumNights(1)
                .build();

        room = new Room();
        room.setId(1L);
        room.setName("Room 1");
        room.setPrice(100.0f);
        room.setCapacity(2);
        room.setRateAppliesTo(2);
        room.setRoomReservations(Stream.of(new RoomReservation())
                .peek(res -> {
                    res.setRoom(room);
                    res.setId(new RoomReservationId(1L, LocalDate.now()));
                    res.setCurrentPrice(100.0f);
                    res.setAvailable(1);
                    res.setMinimumNights(1);
                }).collect(Collectors.toSet()));
    }

    @Test
    void triggerRoomReservationUpdate_success() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();
        List<Long> roomIds = List.of(1L);


        when(roomRepository.findAllById(roomIds)).thenReturn(List.of(room));
        when(roomMapper.toReservationEntities(any())).thenReturn(room.getRoomReservations().stream().toList());
        // Act
        sut.triggerRoomReservationUpdate(startDate, endDate, Optional.of(roomIds));

        // Assert
        verify(roomRepository, times(1)).findAllById(roomIds);
        verify(roomReservationRepository, times(1)).saveAll(room.getRoomReservations().stream().toList());

        assertFalse(room.getRoomReservations().isEmpty());
        Assertions.assertEquals(1, room.getRoomReservations().size());
        room.getRoomReservations().forEach(res -> Assertions.assertEquals(room, res.getRoom()));
    }

    @Test
    void triggerRoomReservationUpdate_noRoomsOnDb() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        when(roomRepository.findAll()).thenReturn(Collections.emptyList());
        Optional<List<Long>> ids = Optional.empty();

        // Act
        assertThrows(HttpClientErrorException.class, () -> sut.triggerRoomReservationUpdate(startDate, endDate, ids));

        // Assert
        verify(roomRepository, times(1)).findAll();

    }

    @Test
    void triggerRoomReservationUpdate_startDateAfterEndDate() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();
        Optional<List<Long>> ids = Optional.empty();
        // Act & Assert
        assertThrows(PastDateException.class,
                () -> sut.triggerRoomReservationUpdate(startDate, endDate, ids));

    }

    @Test
    void testTriggerRoomUpdate() {
        // given
        var mockRoom = mock(RoomDao.class);
        when(roomScraperService.getAllRooms()).thenReturn(List.of(mockRoom));
        when(roomMapper.toRoomEntities(any())).thenReturn(List.of(room));
        when(roomRepository.findById(any())).thenReturn(Optional.ofNullable(room));
        // when
        sut.triggerRoomUpdate();

        // then
        verify(roomScraperService, times(1)).getAllRooms();
        verify(roomRepository, times(1)).saveAll(List.of(room));

    }

}