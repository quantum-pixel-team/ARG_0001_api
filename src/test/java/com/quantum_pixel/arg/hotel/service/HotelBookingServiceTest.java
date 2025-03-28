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
import com.quantum_pixel.arg.v1.web.model.RoomFiltersDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
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
    @Mock
    private ReservationUrlBuilder reservationUrlBuilder;

    @InjectMocks
    private HotelBookingService sut;
    @Mock
    private RoomReservationRepository roomReservationRepository;
    private final Clock clock = Clock.system(ZoneOffset.UTC);

    private Room room;
    RoomReservationDao roomReservationDao;
    RoomFiltersDTO.RoomFiltersDTOBuilder filtersDTO;

    @BeforeEach
    void setUp() {

        // Inject the fixed clock into the HotelBookingService
        sut = new HotelBookingService(roomMapper, roomRepository, roomReservationRepository, roomScraperService, reservationUrlBuilder, clock);

        roomReservationDao = RoomReservationDao.builder()
                .roomId(1L)
                .date(OffsetDateTime.now())
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
                    res.setId(new RoomReservationId(1L, LocalDateTime.now()));
                    res.setCurrentPrice(100.0f);
                    res.setAvailable(1);
                    res.setMinimumNights(1);
                }).collect(Collectors.toSet()));

        filtersDTO = RoomFiltersDTO.builder()
                .pageIndex(0)
                .pageSize(5)
                .checkInDate(OffsetDateTime.now().plusDays(1))
                .checkOutDate(OffsetDateTime.now().plusDays(2))
                .numberOfRooms(1)
                .numberOfAdults(1)
                .childrenAges(List.of(7, 8))
                .roomTypes(Set.of("single", "suite"))
                .minPrice(Optional.of(50.0))
                .maxPrice(Optional.of(500.0))
                .roomFacilities(Set.of("wifi", "pool"))
                .sort(Optional.of(RoomFiltersDTO.SortEnum.ASC));
    }

    @Test
    void triggerRoomReservationUpdate_success() {
        // Arrange
        var startDate = OffsetDateTime.now().minusDays(1);
        var endDate = OffsetDateTime.now();
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
        var startDate = OffsetDateTime.now().minusDays(1);
        var endDate = OffsetDateTime.now();

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
        var startDate = OffsetDateTime.now().plusDays(1);
        var endDate = OffsetDateTime.now();
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


    @Test
    void testGetPaginatedRooms_invalidCheckInRange() {
        filtersDTO.checkInDate(OffsetDateTime.of(LocalDate.of(2024, 7, 19), LocalTime.MIDNIGHT, ZoneOffset.UTC))
                .checkOutDate(OffsetDateTime.of(LocalDate.of(2024, 7, 18), LocalTime.MIDNIGHT, ZoneOffset.UTC))
                .build(); // Invalid date range

        // Verify exception
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            sut.getPaginatedRooms(ZoneOffset.UTC, filtersDTO.build());
        });
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Check-in should not be on past!", thrown.getReason());
    }
    @Test
    void testGetPaginatedRooms_invalidDateInRange() {
        filtersDTO.checkInDate(OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneOffset.UTC))
                .checkOutDate(OffsetDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT, ZoneOffset.UTC))
                .build(); // Invalid date range

        // Verify exception
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            sut.getPaginatedRooms(ZoneOffset.UTC, filtersDTO.build());
        });
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Check-in should not be same with checkout!", thrown.getReason());
    }
    @Test
    void testGetPaginatedRooms_invalidPriceRange() {
        RoomFiltersDTO filtersDTO = this.filtersDTO
                .minPrice(Optional.of(500.0))
                .numberOfAdults(1)
                .numberOfRooms(1)
                .maxPrice(Optional.of(100.0)).build();

        // Verify exception
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            sut.getPaginatedRooms(ZoneOffset.UTC, filtersDTO);
        });
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertEquals("Min price should be less than max price!", thrown.getReason());
    }

    @Test
    void testGetPaginatedRooms_noRoomsFound() {

        when(roomRepository.getRoomAggregated(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Page.empty());

        // Verify exception
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            sut.getPaginatedRooms(ZoneOffset.UTC, filtersDTO.build());
        });
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatusCode());
        assertEquals("No rooms available on Database", thrown.getReason());
    }
}