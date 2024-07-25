package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.TableStructureChangedException;
import com.quantum_pixel.arg.hotel.model.app_in_connect.Rate;
import com.quantum_pixel.arg.hotel.model.app_in_connect.RateDate;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomScraperServiceTest {
    @Mock
    private RoomReservationService roomReservationService;
    @Mock
    private Connection connection;
    @InjectMocks
    private RoomScraperService sut;
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
            var startDate = LocalDate.now();
            var endDate = LocalDate.now();

            // when
            assertThrows(TableStructureChangedException.class,
                    () -> sut.getRoom(startDate, endDate));

        }
    }
    @Test
    void testGetRoom() throws Exception {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 7, 19);
        LocalDate endDate = LocalDate.of(2023, 7, 20);
        try (MockedStatic<Jsoup> sutUtil = Mockito.mockStatic(Jsoup.class)) {


            Document mockDocument = mock(Document.class);
            Element mockTable = mock(Element.class);
            Elements mockRows = mock(Elements.class);
            Element mockRow = mock(Element.class);
            Element mockTd = mock(Element.class);
            Element mockP = mock(Element.class);
            Elements mockTdElements = mock(Elements.class);
            Elements mockPElements = mock(Elements.class);
            Attribute mockAtr = mock(Attribute.class);

            sutUtil.when(() -> Jsoup.connect(any())).thenReturn(connection);
            when(connection.get()).thenReturn(mockDocument);

            when(mockDocument.getElementById("room-chart")).thenReturn(mockTable);
            when(mockTable.select("tr")).thenReturn(mockRows);
            when(mockRows.stream()).thenReturn(Stream.of(mockRow, mockRow));
            when(mockRow.attribute("data-rate-id")).thenReturn(mockAtr);
            when(mockAtr.getValue()).thenReturn("1");
            when(mockRow.getElementsByTag("td")).thenReturn(mockTdElements);
            when(mockTdElements.getFirst()).thenReturn(mockTd);
            when(mockTd.getElementsByTag("p")).thenReturn(mockPElements);
            when(mockPElements.getFirst()).thenReturn(mockP);
            when(mockP.text()).thenReturn("Room Name");

            when(roomReservationService.fetchRoomRates(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                    .thenReturn(mockRate());


            // Act
            List<RoomDao> roomDaos = sut.getRoom(startDate, endDate);

            // Assert
            assertNotNull(roomDaos);
            assertEquals(1, roomDaos.size());
            assertEquals("Room Name", roomDaos.getFirst().getName());
        }
    }

    private Rate mockRate() {
        RateDate rateDate = new RateDate();
        rateDate.setDate(LocalDate.of(2023, 7, 19));
        rateDate.setRate("100.0");
        rateDate.setCapacity(2);
        rateDate.setGuests(2);
        rateDate.setAvail(10);
        rateDate.setMinStay(1);

        Rate rate = new Rate();
        rate.setRateId(1L);
        rate.setDates(List.of(rateDate));

        return rate;
    }
}