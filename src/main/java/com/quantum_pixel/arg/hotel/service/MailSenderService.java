package com.quantum_pixel.arg.hotel.service;

import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.mail.ConferenceMailStructure;
import com.quantum_pixel.arg.hotel.model.mail.MailStructure;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class MailSenderService {
    private static final String email = "lukabuziu42@gmail.com";

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String EMAIL;

    @SneakyThrows
    public void sendEmail(MailStructure mailStructure) {
        verifyReservationDates(mailStructure)
                .createSimpleEmail(mailStructure);

    }

    private MailSenderService verifyReservationDates(MailStructure mailStructure) throws PastDateException{
        LocalDate date=LocalDate.now();
        ConferenceMailStructure conferenceMailStructure= (ConferenceMailStructure) mailStructure;
         conferenceMailStructure.getConferenceReservations()
                .stream().filter(reservation ->
                         reservation.getReservationDate().isBefore(date)
                                   || reservation.getStartTime().isAfter(reservation.getEndTime())
                ).findFirst()
                 .ifPresent(sam -> {
                     System.out.println("is throwing exception");
                     throw new PastDateException();
                 });
        return this;
    }

    private void createSimpleEmail(MailStructure mailStructure){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(mailStructure.createEmailSubject());
        simpleMailMessage.setFrom(EMAIL);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(mailStructure.createEmailContext());
        javaMailSender.send(simpleMailMessage);
    }


}
