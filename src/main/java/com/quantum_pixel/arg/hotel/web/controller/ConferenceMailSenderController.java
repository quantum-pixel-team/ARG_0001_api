package com.quantum_pixel.arg.hotel.web.controller;

import com.quantum_pixel.arg.hotel.model.mail.MailStructure;
import com.quantum_pixel.arg.hotel.service.MailSenderService;
import com.quantum_pixel.arg.hotel.web.mapper.ConferenceMailMapper;
import com.quantum_pixel.arg.v1.web.ContactUsApi;

import com.quantum_pixel.arg.v1.web.model.ConfernceMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConferenceMailSenderController implements ContactUsApi {
    private final MailSenderService mailSenderService;
    private final ConferenceMailMapper conferenceMailMapper;
    @Override
    public ResponseEntity<Void> sendEmail(ConfernceMailStructureDTO confernceMailStructureDTO) {
        MailStructure object = conferenceMailMapper.toObject(confernceMailStructureDTO);
        mailSenderService.sendEmail(object);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
