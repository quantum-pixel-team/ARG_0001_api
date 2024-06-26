package com.quantum_pixel.arg.whatsapp.controller;

import com.quantum_pixel.arg.v1.web.WhatsappApi;
import com.quantum_pixel.arg.v1.web.model.RestaurantReservationRequestDTO;
import com.quantum_pixel.arg.whatsapp.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WhatsAppController implements WhatsappApi {

    private final WhatsAppService whatsappService;
    @Override
    public ResponseEntity<Void> sendRestaurantReservation(RestaurantReservationRequestDTO request) {
        whatsappService.sendRestaurantReservationDetails(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
