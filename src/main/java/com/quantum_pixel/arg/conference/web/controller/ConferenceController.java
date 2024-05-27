package com.quantum_pixel.arg.conference.web.controller;

import com.quantum_pixel.arg.conference.service.ConferenceService;
import com.quantum_pixel.arg.v1.web.ConferenceReservationApi;
import com.quantum_pixel.arg.v1.web.ContactUsApi;
import com.quantum_pixel.arg.v1.web.model.ConfernceMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConferenceController implements ConferenceReservationApi {
    private final ConferenceService conferenceService;

    @Override
    public ResponseEntity<Void> sendConferenceReservationMail(ConfernceMailStructureDTO confernceMailStructureDTO) {
        conferenceService.sentEmail(confernceMailStructureDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
