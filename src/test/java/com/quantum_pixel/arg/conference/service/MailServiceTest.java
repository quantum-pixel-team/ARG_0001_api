package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.model.ConferenceMailStructure;
import com.quantum_pixel.arg.conference.model.MailStructure;
import com.quantum_pixel.arg.conference.model.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    @InjectMocks
    private MailService mailService;
    @Mock
    private JavaMailSender mailSender;

    private static final String email = "lukabuziu42@gmail.com";

    @Test
    void emailSentSuccessfully() {
//        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
//        MailStructure emailStructure = createEmailStructure();
//        SimpleMailMessage simpleMail = createSimpleMail();
//        mailService.sendEmail(emailStructure);
//        System.out.println(emailStructure.createEmailContext());
//        verify(mailSender, times(1)).send(argumentCaptor.capture());
//        SimpleMailMessage emailSent = argumentCaptor.getValue();
//        assertThat(emailSent.getSubject()).isNotNull()
//                .isEqualTo(simpleMail.getSubject());
//        assertThat(emailSent.getText())
//                .isEqualTo(simpleMail.getText());

    }


    @Test
    void emailThrowsMailException() {
//        MailStructure emailStructure = createEmailStructure();
//        doThrow(new MailParseException("")).when(mailSender).send((SimpleMailMessage) any());
//        Assertions.assertThrows(MailException.class, () -> mailService.sendEmail(emailStructure));
    }




    private SimpleMailMessage createSimpleMail() {
        MailStructure emailStructure = createEmailStructure();
        SimpleMailMessage message = new SimpleMailMessage();
        String EMAIL = "some03642@gmail.com";
        message.setFrom(EMAIL);
        message.setSubject(emailStructure.createEmailSubject());
        message.setTo(email);
        message.setText(emailStructure.createEmailContext());
        return message;
    }

    private MailStructure createEmailStructure() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add((Reservation.builder()
                .reservationDate(LocalDate.MAX)
                .startTime(LocalTime.MIN)
                .endTime(LocalTime.MAX)
                .build()));
        return ConferenceMailStructure.builder()
                .fullNameOrCompanyName("luka")
                .email("lukabuziu42@gmail.com")
                .phoneNumber(Optional.of("0682510985"))
                .conferenceReservations(reservations)
                .emailContent(Optional.of("content"))
                .build();
    }
}