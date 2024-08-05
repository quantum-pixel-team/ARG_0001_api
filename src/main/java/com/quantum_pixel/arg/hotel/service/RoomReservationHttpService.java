package com.quantum_pixel.arg.hotel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quantum_pixel.arg.hotel.model.app_in_connect.Rate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RoomReservationHttpService {

    private final RestTemplate restTemplate;


    @SneakyThrows
    public Rate fetchRoomRates(Long roomId, LocalDate startDate, LocalDate endDate) {

        String url = "https://app.inn-connect.com/book2/Book/book";
        HttpEntity<String> entity = getStringHttpEntity(roomId, startDate, endDate);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();
        if (responseBody == null) throw new BadRequestException("Invalid response");

        Pattern pattern = Pattern.compile("\\[\"(.*?)\"\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(responseBody);

        if (matcher.find()) {
            String json = matcher.group(1).replace("\\", "");

            ObjectMapper objectMapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build();

            List<Rate> rates = objectMapper.readValue(json, new TypeReference<>() {
            });
            return rates.getFirst();
        } else throw new BadRequestException("Invalid response");
    }

    private static HttpEntity<String> getStringHttpEntity(Long roomId, LocalDate startDate, LocalDate endDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/x-gwt-rpc; charset=UTF-8");
        headers.set("X-Gwt-Permutation", "9AE23D7F06C0B6A948000F4A953CFF1F");

        String startYear = String.valueOf(startDate.getYear()).substring(2);
        String endYear = String.valueOf(endDate.getYear()).substring(2);
        int endDay = 31;
        int endMonth = endDate.getMonthValue() - 1;

        var startMonth = startDate.getMonthValue() - 1;
        int startDay = 1;

        String requestBody = String.format(
                "7|0|6|https://app.inn-connect.com/book2/Book/|2D6CD36E64D6CD2E3CC7CE95346384EB|com.vega.client.BookService|getAvaCalendar|I|java.util.Date/3385151746|1|2|3|4|4|5|6|6|5|625612|6|1%s|%d|%d|0|0|0|6|1%s|%d|%d|0|0|0|%d|",
                startYear, startMonth, startDay, endYear, endMonth, endDay, roomId
        );

        return new HttpEntity<>(requestBody, headers);
    }
}
