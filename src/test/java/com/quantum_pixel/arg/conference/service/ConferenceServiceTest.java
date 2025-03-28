package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.web.mapper.ConferenceMapper;
import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.v1.web.model.ConferenceMailStructureDTO;
import com.quantum_pixel.arg.v1.web.model.ReservationDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class ConferenceServiceTest {
    @Mock
    private MailService mailService;
    @InjectMocks
    private ConferenceService conferenceService;
    @Spy
    private ConferenceMapper conferenceMapper = Mappers.getMapper(ConferenceMapper.class);


    @Test
    void emailThrowsPasDateExceptionForDate() {

        var emailStructure = createEmailStructure();
        emailStructure.getConferenceReservations().add(ReservationDTO.builder()
                .reservationDate(OffsetDateTime.now().minusDays(2))
                .startTime(OffsetDateTime.now().minusDays(2))
                .endTime(OffsetDateTime.now().minusDays(20))
                .build());


        Assertions.assertThrows(PastDateException.class, () -> conferenceService.sentEmail(emailStructure));

    }

    @Test
    void emailThrowsPastDateExceptionForTime() {

        var emailStructure = createEmailStructure();
        emailStructure.getConferenceReservations().add(ReservationDTO.builder()
                .reservationDate(OffsetDateTime.now().minusDays(5))
                .startTime(OffsetDateTime.now().minusDays(5))
                .endTime(OffsetDateTime.now().plusDays(20))
                .build());
        Assertions.assertThrows(PastDateException.class, () -> conferenceService.sentEmail(emailStructure));
    }

    @Test
    void emailValidationPassSuccessfully() {
        var confernceMailStructureDTO = createEmailStructure();
        doNothing().when(mailService).sendEmail(any());
        conferenceService.sentEmail(confernceMailStructureDTO);

    }


    private ConferenceMailStructureDTO createEmailStructure() {
        List<ReservationDTO> reservations = new ArrayList<>();
        reservations.add((ReservationDTO.builder()
                .reservationDate(OffsetDateTime.now().plusDays(1))
                .startTime(OffsetDateTime.now().plusDays(2))
                .endTime(OffsetDateTime.now().plusDays(20).plusHours(1))
                .build()));
        return ConferenceMailStructureDTO.builder()
                .fullNameOrCompanyName("luka buziu")
                .email("lukabuziu42@gmail.com")
                .phoneNumber(Optional.of("0682510985"))
                .conferenceReservations(reservations)
                .emailContent(Optional.of("emailContent"))
                .build();
    }
}
