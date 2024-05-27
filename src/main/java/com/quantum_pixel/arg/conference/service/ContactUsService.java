package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.web.mapper.ContactUsMapper;
import com.quantum_pixel.arg.v1.web.model.ContactUsMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactUsService {

    private final MailService mailService;
    private final ContactUsMapper contactUsMapper;

    public void sendContactUsMail(ContactUsMailStructureDTO contactUsMailStructureDTO){
        var mailStructure = contactUsMapper.toEntity(contactUsMailStructureDTO);
        mailService.sendEmail(mailStructure);
    }
}
