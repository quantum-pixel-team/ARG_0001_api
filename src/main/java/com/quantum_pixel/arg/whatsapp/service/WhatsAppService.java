package com.quantum_pixel.arg.whatsapp.service;

import com.quantum_pixel.arg.utilities.DateTimeUtils;
import com.quantum_pixel.arg.utilities.FileUtils;
import com.quantum_pixel.arg.utilities.TemplateUtils;
import com.quantum_pixel.arg.v1.web.model.RestaurantReservationRequestDTO;
import com.quantum_pixel.arg.whatsapp.config.WhatsAppConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class WhatsAppService {

    private final WhatsAppConfig config;
    private final RestTemplate restTemplate;

    public void sendRestaurantReservationDetails(RestaurantReservationRequestDTO reservationRequestDTO) {

        String baseUrl = config.getApiUrl();
        String token = config.getApiToken();
        var phoneNumberId = config.getPhoneNumberId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        log.info("[Whatsapp service] Sending Restaurant reservation {}", reservationRequestDTO);

        var jsonTemplate = FileUtils.readResource("whatsapp/templates/restaurant_reservation.json");
        LocalDate reservationDate = DateTimeUtils.toLocalDateTimeTirane(reservationRequestDTO.getDate()).toLocalDate();

        var reservationTime = DateTimeUtils.toLocalDateTimeTirane(
                        OffsetDateTime.of(reservationRequestDTO.getDate().toLocalDate(),
                                reservationRequestDTO.getTime().toLocalTime(),
                                reservationRequestDTO.getTime().getOffset()))
                .toLocalTime();
        Map<String, String> variables = Map.of(
                "to", reservationRequestDTO.getTo(),
                "language_code", reservationRequestDTO.getLanguageCode().orElse(RestaurantReservationRequestDTO.LanguageCodeEnum.EN).toString(),
                "user_name", reservationRequestDTO.getName(),
                "phone_number", reservationRequestDTO.getPhoneNumber(),
                "guests", reservationRequestDTO.getGuests().toString(),
                "reservation_date", reservationDate.toString(),
                "reservation_time", reservationTime.toString(),
                "message", reservationRequestDTO.getMessage().map(el -> el.isBlank() ? null : el).orElse("NaN")
        );
        var body = TemplateUtils.injectVariables(jsonTemplate, variables);

        log.debug(body);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForObject(baseUrl + "/" + phoneNumberId + "/messages", request, String.class);
    }
}
