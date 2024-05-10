package com.quantum_pixel.arg.conference.web.controller;

import com.quantum_pixel.arg.conference.service.ConferenceService;
import com.quantum_pixel.arg.hotel.model.mail.ConferenceMailStructure;
import com.quantum_pixel.arg.v1.web.ContactUsApi;
import com.quantum_pixel.arg.v1.web.model.ConferenceMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConferenceController implements ContactUsApi {
    private final ConferenceService conferenceService;

    @Override
    public ResponseEntity<Void> sendEmail(ConferenceMailStructureDTO conferenceMailStructureDTO) {
        conferenceService.sentEmail(conferenceMailStructureDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
