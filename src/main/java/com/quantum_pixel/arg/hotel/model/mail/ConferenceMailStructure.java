package com.quantum_pixel.arg.hotel.model.mail;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
@SuperBuilder
public class ConferenceMailStructure extends MailStructure{
    private String email;
    private Optional<String> phoneNumber;
    private List<Reservation> conferenceReservations;

    private static final String SUBJECT ="Kerkese aprovimi per rezervim te konferencën: %s";
    private static final String CONTEXT_FIRST_PART= "Të dhënat për rezervimin janë si vijon:\n" +
            "\n" +
            "Emri: %s\n" +
            "Mbiemri: %s\n" +
            "Email: %s\n" ;
    private static final String CONTEXT_NUMBER_PHONE =
            "Numri i telefonit: %s\n" ;
    private static final String CONTEXT_LAST_PART =
            "Datat e mundshme të konferencës:\n" +
             "%s"+
            "Pershkrimi i rezervimit: %s"
            +"Me respekt," +
            "Quantum-Pixel";


    @Override
    public String createEmailSubject() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formattedDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String dateTime = currentDateTime.format(formattedDateTime);
        return String.format(SUBJECT,dateTime);
    }

    @Override
    public String createEmailContext() {
        String reservationDate = conferenceReservations.stream()
                .map(Reservation::toString)
                .collect(Collectors.joining("\n"));
        String firstMailPartStructure = String.format(CONTEXT_FIRST_PART,
                getFirstName(), getLastName()
                , email);

        return phoneNumber.map(s -> firstMailPartStructure + String.format(CONTEXT_NUMBER_PHONE, s)
                + String.format(CONTEXT_LAST_PART, reservationDate, getEmailContent())).orElseGet(()
                -> firstMailPartStructure + String.format(CONTEXT_LAST_PART, reservationDate
                , getEmailContent()));
    }

    @Override
    public String toString() {
        return "ConferenceMailStructure{" +
                "email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", conferenceReservations=" + conferenceReservations +
                "emri :" + getFirstName() +
                "mbiemri : " + getFirstName() +
                 "desc" + getEmailContent() +
                '}';
    }
}
