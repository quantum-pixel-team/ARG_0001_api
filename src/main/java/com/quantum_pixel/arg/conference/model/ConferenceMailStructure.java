package com.quantum_pixel.arg.conference.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
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
    private Optional<String> emailContent;

    private static final String SUBJECT ="Njoftim për Rezervim Konference: %s";
    private static final String CONTEXT_FIRST_PART= "Përshëndetje,\n" +
            "\n" +
            "Ju informojmë se është bërë një kërkesë rezervimi për konferencën me këto detaje::\n" +
            "\n" +
            "Emri i Plotë: %s\n" +
            "Email: %s\n" ;
    private static final String CONTEXT_NUMBER_PHONE =
            "Numri i Telefonit: %s\n" ;
    private static final String EMAIL_CONTENT =
            "Detajet e Rezervimit::\n" +
             "%s"+
            "Mesazh Shtesë: %s"
            +"Për të kontaktuar klientin, ju lutemi përdorni informacionin e mësipërm.\n" +
                    "\n" +
                    "Ju lutemi mos i bëni reply këtij emaili.\n" +
                    "\n" +
                    "Faleminderit.";


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
                getFullNameOrCompanyName()
                , email);

        return phoneNumber.map(s -> firstMailPartStructure + String.format(CONTEXT_NUMBER_PHONE, s)
                + String.format(EMAIL_CONTENT, reservationDate, getEmailContent()))
                .orElseGet(() -> firstMailPartStructure + String.format(EMAIL_CONTENT, reservationDate
                , getEmailContent().orElse("Nuk Ka Pershkrim")));
    }

    @Override
    public String toString() {
        return "ConferenceMailStructure{" +
                "email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", conferenceReservations=" + conferenceReservations +
                "emri :" + getFullNameOrCompanyName() +
                 "desc" + getEmailContent() +
                '}';
    }
}
