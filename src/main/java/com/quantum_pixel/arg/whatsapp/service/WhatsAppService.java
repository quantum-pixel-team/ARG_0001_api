package com.quantum_pixel.arg.whatsapp.service;

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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class WhatsAppService {

    private final WhatsAppConfig config;

    public void sendRestaurantReservationDetails(RestaurantReservationRequestDTO reservationRequestDTO) {
        RestTemplate restTemplate = new RestTemplate();

        String baseUrl = config.getApiUrl();
        String token = config.getApiToken();
        String to = config.getTo();
        var phoneNumberId = config.getPhoneNumberId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        var jsonTemplate = FileUtils.readResource("whatsapp/templates/restaurant_reservation.json");
        Map<String, String> variables = Map.of(
                "to", to,
                "language_code", reservationRequestDTO.getLanguageCode().orElse(RestaurantReservationRequestDTO.LanguageCodeEnum.EN).toString(),
                "user_name", reservationRequestDTO.getName(),
                "phone_number", reservationRequestDTO.getPhoneNumber(),
                "guests", reservationRequestDTO.getGuests().toString(),
                "reservation_date", reservationRequestDTO.getDate().toString(),
                "reservation_time", reservationRequestDTO.getTime().toString(),
                "message", reservationRequestDTO.getMessage().orElse("NaN")
        );
        var body = TemplateUtils.injectVariables(jsonTemplate, variables);

        log.debug("[Whatsapp service] Sending message using body: ");
        log.debug(body);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForObject(baseUrl + "/" + phoneNumberId + "/messages", request, String.class);
    }
}
