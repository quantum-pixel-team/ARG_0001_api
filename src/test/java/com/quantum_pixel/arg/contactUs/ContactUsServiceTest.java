package com.quantum_pixel.arg.contactUs;

import com.quantum_pixel.arg.conference.service.ContactUsService;
import com.quantum_pixel.arg.conference.service.MailService;
import com.quantum_pixel.arg.conference.web.mapper.ContactUsMapper;
import com.quantum_pixel.arg.v1.web.model.ContactUsMailStructureDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

public class ContactUsServiceTest {
    @Mock
    private MailService mailService;
    @InjectMocks
    private ContactUsService contactUsService;
    @Spy
    private ContactUsMapper conferenceMapper = Mappers.getMapper(ContactUsMapper.class);

    @Test
    void contactUsMailIsSent(){
        contactUsService.sendContactUsMail(new ContactUsMailStructureDTO(
                "luka","lukabuziu@gmail.com",
                "some info from the client"));
        verify(mailService,times(1)).sendEmail( any());
    }
}
