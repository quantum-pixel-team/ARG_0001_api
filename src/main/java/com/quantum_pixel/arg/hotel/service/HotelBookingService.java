package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.TableStructureChangedException;
import com.quantum_pixel.arg.hotel.model.RoomPrototype;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.web.mapper.HotelRoomMapper;
import com.quantum_pixel.arg.v1.web.model.RoomPrototypeDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class HotelBookingService {

    private static final String BASE_URL = "https://app.inn-connect.com/book/properties/Aragosta%20Hotel%26Restaurant";

    private final HotelRoomMapper roomMapper;

    @SneakyThrows
    public List<RoomPrototypeDTO> getRoomReservationsForGivenRangeOfDate(Optional<LocalDate> requestedStartDate, Optional<LocalDate> requestedEndDate) {

        var startDate = requestedStartDate.orElse(LocalDate.now());
        var endDate = requestedEndDate.orElse(LocalDate.now().plusDays(14));

        int totalRequestedDays = endDate.getDayOfYear() - startDate.getDayOfYear();
        if (totalRequestedDays < 0) throw new BadRequestException("End data should be greater than start date");

        log.info("Getting room reservation details");
        var totalBiweek = (int) Math.ceil(totalRequestedDays / 14.0);
        var roomReservationMap = IntStream.range(0, totalBiweek)
                .mapToObj(biWeekNumber -> startDate.plusDays(biWeekNumber * 14L))
                .flatMap(startDate1 -> getRoomPrototypeFromUrl(startDate1).stream())
                .filter(roomReservation -> roomReservation.date().isBefore(endDate) || roomReservation.date().isEqual(endDate))
                .collect(Collectors.groupingBy(RoomReservation::name));
        List<RoomPrototype> roomPrototypes = roomReservationMap.entrySet()
                .stream()
                .map(entry -> {
                    List<RoomReservation> reservations = entry.getValue();
                    Integer price = reservations.stream().findAny().map(RoomReservation::currentPrice).orElse(null);
                    return RoomPrototype.builder()
                            .name(entry.getKey())
                            .price(price)
                            .roomReservations(reservations)
                            .build();
                }).toList();
        return roomMapper.toDTOList(roomPrototypes);
    }

    @SneakyThrows
    private List<RoomReservation> getRoomPrototypeFromUrl(LocalDate startDate) {

        var url = BASE_URL + "?start_date=" + startDate;
        log.info("Sending Request to get room reservation using url: " + url);
        Document document = Jsoup.connect(url).get();
        // getting tale from doc
        Element table = document.getElementById("room-chart");
        // getting table body
        if(table == null) {
            throw new TableStructureChangedException();
        }
        Elements rows = table.select("tr");
        return rows.stream().skip(1)
                .flatMap(row -> {
                    var cells = row.select("td");
                    int roomPrice = Integer.parseInt(row.attribute("data-prev-day-rate").getValue());

                    String name = Objects.requireNonNull(cells.get(0).selectFirst("p")).text();
                    return cells.stream()
                            .skip(1)
                            .limit(14)
                            .map(cell -> {
                                String date = cell.attribute("data-tooltip").getValue().split(" ")[1];
                                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                                Element a = cell.selectFirst("a");
                                var sold = a == null;
                                int price = roomPrice;

                                String reservationUrl = null;
                                if (a != null) {
                                    reservationUrl = BASE_URL + a.attribute("href").getValue();
                                    price = Integer.parseInt(a.text());
                                }
                                return new RoomReservation(name, localDate, sold, price, roomPrice, reservationUrl);
                            });
                }).toList();


    }




}

