package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.TableStructureChangedException;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapperImpl;
import com.quantum_pixel.arg.v1.web.model.RoomPrototypeDTO;
import com.quantum_pixel.arg.v1.web.model.RoomReservationDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelBookingServiceTest {

    @Mock
    private Connection connection;

    HotelRoomMapper roomMapper = new HotelRoomMapperImpl();
    private final HotelBookingService sut = new HotelBookingService(roomMapper);

    @Test
    void getRoomReservationsForGivenRangeOfDate_table_structure_changed() throws IOException {
        try (MockedStatic<Jsoup> sutUtil = Mockito.mockStatic(Jsoup.class)) {

            // given
            Document document = new Document("");
            sutUtil.when(() -> Jsoup.connect(any())).thenReturn(connection);
            when(connection.get()).thenReturn(document);

            Element table = new Element("table");
            table.attr("id", "new table id");
            document.appendChild(table);
            Optional<LocalDate> startDate = Optional.empty();
            Optional<LocalDate> endDate = Optional.empty();

            // when
            assertThrows(TableStructureChangedException.class,
                    () -> sut.getRoomReservationsForGivenRangeOfDate(startDate, endDate));

        }
    }

    @Test
    void getRoomReservationsForGivenRangeOfDate_ok() throws IOException {
        // given
        try (MockedStatic<Jsoup> sutUtil = Mockito.mockStatic(Jsoup.class)) {

            Document document = mock(Document.class);
            sutUtil.when(() -> Jsoup.connect(any())).thenReturn(connection);
            when(connection.get()).thenReturn(document);
            Element mockTable = mock(Element.class);
            when(document.getElementById("room-chart")).thenReturn(mockTable);

            Element mockRow = new Element("tr");
            mockRow.attr("data-prev-day-rate", "40");

            Element cell = new Element("td");
            cell.attr("data-tooltip", "date " + LocalDate.now());
            Element cellHeader = new Element("td");

            Element p = new Element("p");
            cellHeader.appendChild(p);
            Element cell2 = new Element("td");
            cell2.attr("data-tooltip", "date " + LocalDate.now().plusDays(1));
            Element a = new Element("a");
            a.attr("href", "url_to_room_reservation");
            a.text("150");
            cell2.appendChild(a);
            mockRow.appendChild(cellHeader);
            mockRow.appendChild(cell);
            mockRow.appendChild(cell2);
            Elements rows = new Elements(mockRow, mockRow);
            when(mockTable.select("tr")).thenReturn(rows);

            // when
            List<RoomPrototypeDTO> roomReservations = sut.getRoomReservationsForGivenRangeOfDate(Optional.empty(), Optional.empty());

            // then

            RoomReservationDTO reservationDetails = roomReservations.get(0).getRoomReservations().get(0);

            assertThat(reservationDetails)
                    .extracting(RoomReservationDTO::getDate)
                    .isEqualTo(LocalDate.now());
            assertThat(reservationDetails)
                    .extracting(RoomReservationDTO::getSold)
                    .withFailMessage("This room should be sold on given date since a tag is null")
                    .isEqualTo(true);
            assertThat(roomReservations.get(0).getRoomReservations().get(1))
                    .extracting(RoomReservationDTO::getSold)
                    .isEqualTo(false);
        }


    }
}