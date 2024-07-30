package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.model.ConferenceMailStructure;
import com.quantum_pixel.arg.conference.web.mapper.ConferenceMapper;
import com.quantum_pixel.arg.v1.web.model.ConferenceMailStructureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    private void verifyReservationDates(ConferenceMailStructure conferenceMailStructure)  {
        LocalDate date = LocalDate.now();
        conferenceMailStructure.getConferenceReservations()
                .stream().filter(reservation ->
                        reservation.getReservationDate().isBefore(date.minusDays(1))
                                || reservation.getStartTime().isAfter(reservation.getEndTime())
                ).findFirst()
                .ifPresent(sam -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Reservation date can not be in the past");
                });

    }


}
