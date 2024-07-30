package com.quantum_pixel.arg.hotel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ReservationUrlBuilder {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public String buildReservationUrl(LocalDate checkInDate,
                                      LocalDate checkOutDate,
                                      Integer numberOfRooms,
                                      Integer numberOfAdults,
                                      List<Integer> childrenAges,
                                      Long roomId) {

        var baseUrl = "https://app.inn-connect.com/book2/?p=Aragosta+Hotel%26Restaurant#book";
        var chAges = String.join(",", childrenAges.stream().map(Object::toString).toList());

        List<RoomDetails> rooms = IntStream.range(0, numberOfRooms)
                .mapToObj(el -> new RoomDetails(numberOfAdults, childrenAges.size(), roomId, chAges)).toList();
        BookingDetails bookingDetails = new BookingDetails(checkInDate.toString(), checkOutDate.toString(), "USD", "en", rooms);

        String jsonValue = objectMapper.writeValueAsString(bookingDetails);
        String encoded = URLEncoder.encode(jsonValue, StandardCharsets.UTF_8);
        encoded = encoded.replace("%3A", ":").replace("%2C", ",");
        return baseUrl + encoded;
    }

    // This record represents the booking details for URL encoding
    record BookingDetails(String ci, String co, String curr, String lang, List<RoomDetails> rooms) {
    }

    // This record represents the room details within the booking for URL encoding
    record RoomDetails(Integer adults, Integer children, Long rate, String chAges) {
    }
}
