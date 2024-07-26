package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.RoomReservationId;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.model.dao.RoomReservationDao;
import com.quantum_pixel.arg.hotel.repository.RoomRepository;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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


    private RoomDao roomDao;
    private Room room;

    @BeforeEach
    void setUp() {
        RoomReservationDao roomReservationDao = RoomReservationDao.builder()
                .roomId(1L)
                .date(LocalDate.now())
                .currentPrice(100.0)
                .available(1)
                .minimumNights(1)
                .build();

        roomDao = RoomDao.builder()
                .id(1L)
                .name("Room 1")
                .price(100.0)
                .capacity(2)
                .rateAppliesTo(2)
                .roomReservations(List.of(roomReservationDao))
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
    void triggerRoomReservationUpdate_success() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        when(roomScraperService.getRoom(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(roomDao));
        when(roomMapper.toEntity(anyList()))
                .thenReturn(List.of(room));

        // Act
        sut.triggerRoomReservationUpdate(startDate, endDate);

        // Assert
        verify(roomScraperService, times(1)).getRoom(startDate, endDate);
        verify(roomMapper, times(1)).toEntity(List.of(roomDao));
        verify(roomRepository, times(1)).saveAll(List.of(room));

        assertFalse(room.getRoomReservations().isEmpty());
        assertEquals(1, room.getRoomReservations().size());
        room.getRoomReservations().forEach(res -> assertEquals(room, res.getRoom()));
    }

    @Test
    void triggerRoomReservationUpdate_startDateAfterEndDate() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now();

        // Act & Assert
        assertThrows(PastDateException.class,
                () -> sut.triggerRoomReservationUpdate(startDate, endDate));

    }

}