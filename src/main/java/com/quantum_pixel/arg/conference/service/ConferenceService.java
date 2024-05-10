package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.web.mapper.ConferenceMapper;
import com.quantum_pixel.arg.hotel.exception.PastDateException;
import com.quantum_pixel.arg.hotel.model.mail.ConferenceMailStructure;
import com.quantum_pixel.arg.v1.web.model.ConferenceMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConferenceService {

    private final MailService mailService;

    private final ConferenceMapper conferenceMapper;

    public void sentEmail(ConferenceMailStructureDTO conferenceMailStructure) {
        var conferenceMapperEntity = conferenceMapper.toEntity(conferenceMailStructure);
        verifyReservationDates(conferenceMapperEntity);
        mailService.sendEmail(conferenceMapperEntity);
    }

    private void verifyReservationDates(ConferenceMailStructure conferenceMailStructure) throws PastDateException {
        LocalDate date = LocalDate.now();
        conferenceMailStructure.getConferenceReservations()
                .stream().filter(reservation ->
                        reservation.getReservationDate().isBefore(date)
                                || reservation.getStartTime().isAfter(reservation.getEndTime())
                ).findFirst()
                .ifPresent(sam -> {
                    throw new PastDateException("Reservation date can not be in the past");
                });

    }


}
