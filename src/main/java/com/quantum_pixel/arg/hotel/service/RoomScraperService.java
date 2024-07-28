package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.TableStructureChangedException;
import com.quantum_pixel.arg.hotel.model.app_in_connect.Rate;
import com.quantum_pixel.arg.hotel.model.app_in_connect.RateDate;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.model.dao.RoomReservationDao;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoomScraperService {

    private final RoomReservationHttpService roomReservationHttpService;

    private static final String BASE_URL = "https://app.inn-connect.com/book/properties/Aragosta%20Hotel%26Restaurant";
    private static final String DEFAULT_PARAM = "widget=1&channel=";


    @SneakyThrows
    public List<RoomDao> getAllRooms() {

        var url = BASE_URL + "?" + DEFAULT_PARAM;

        log.info("Sending Request to get room reservation using url: {}", url);


        Document document = Jsoup.connect(url).get();
        // getting tale from doc
        Element table = document.getElementById("room-chart");
        // getting table body
        if (table == null) {
            throw new TableStructureChangedException();
        }
        Elements rows = table.select("tr");
        return rows.stream().skip(1)
                .map(row -> {
                    var roomId = Long.parseLong(row.attribute("data-rate-id").getValue());
                    String name = row.getElementsByTag("td").getFirst().getElementsByTag("p").getFirst().text();
                    return getRoomDetails(roomId, name);
                }).toList();
    }

    private RoomDao getRoomDetails(Long roomId, String name) {

        Rate rates = roomReservationHttpService.fetchRoomRates(roomId, LocalDate.now(), LocalDate.now().plusDays(1));
        RateDate firstRateDate = rates.getDates().getFirst();
        return RoomDao.builder()
                .id(rates.getRateId())
                .name(name)
                .price(Double.valueOf(firstRateDate.getRate()))
                .capacity(firstRateDate.getCapacity())
                .rateAppliesTo(firstRateDate.getGuests())
                .build();
    }

    public List<RoomReservationDao> getRoomReservationDetails(Long roomId, LocalDate startDate, LocalDate endDate) {

        Rate rates = roomReservationHttpService.fetchRoomRates(roomId, startDate, endDate);
        return rates.getDates().stream()
                .filter(rate -> rate.getDate().isBefore(endDate.plusDays(1)) && rate.getDate().isAfter(startDate.minusDays(1)))
                .map(rateDate -> RoomReservationDao.builder()
                        .roomId(roomId)
                        .date(rateDate.getDate())
                        .currentPrice(Double.parseDouble(rateDate.getRate()))
                        .available(rateDate.getAvail())
                        .minimumNights(rateDate.getMinStay())
                        .build())
                .toList();

    }


}
