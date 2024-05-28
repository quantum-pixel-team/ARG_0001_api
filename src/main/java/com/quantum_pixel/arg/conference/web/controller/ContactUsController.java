package com.quantum_pixel.arg.conference.web.controller;

import com.quantum_pixel.arg.conference.service.ContactUsService;
import com.quantum_pixel.arg.v1.web.ContactUsApi;
import com.quantum_pixel.arg.v1.web.model.ContactUsMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContactUsController implements ContactUsApi {
    private final ContactUsService mailService;
    @Override
    public ResponseEntity<Void> sendContactUsMail(ContactUsMailStructureDTO contactUsMailStructureDTO) {
        mailService.sendContactUsMail(contactUsMailStructureDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
