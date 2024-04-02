package com.quantum_pixel.arg.conference.service;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.quantum_pixel.arg.ConfigTest;
import com.quantum_pixel.arg.hotel.model.mail.ConferenceMailStructure;
import com.quantum_pixel.arg.hotel.model.mail.MailStructure;
import com.quantum_pixel.arg.hotel.model.mail.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class MailServiceIT extends ConfigTest {
    @RegisterExtension
    static GreenMailExtension greenMailExtension = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("luka", "buziu"))
            .withPerMethodLifecycle(true);
    @Autowired
    private  MailService mailService;


    @Test
    void shouldSentSuccessfully(){
        var confernceMailStructure = createEmailStructure();
        mailService.sendEmail(confernceMailStructure);
        assertThat(greenMailExtension.getReceivedMessages().length)
                .isEqualTo(1);
    }


    private MailStructure createEmailStructure() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add((Reservation.builder()
                .reservationDate(LocalDate.MAX)
                .startTime(LocalTime.MIN)
                .endTime(LocalTime.MAX)
                .build()));
        return ConferenceMailStructure.builder()
                .firstName("luka")
                .lastName("Buziu")
                .email("lukabuziu42@gmail.com")
                .phoneNumber(Optional.of("0682510985"))
                .conferenceReservations(reservations)
                .emailContent("more detail information about email")
                .build();
    }

    @Override
    protected List<String> getTableToTruncate() {
        return null;
    }
}
