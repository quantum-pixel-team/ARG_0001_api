package com.quantum_pixel.arg.hotel.model.mail;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@RequiredArgsConstructor
@Data
@ToString
@SuperBuilder
public abstract class MailStructure {
    private String firstName;
    private String lastName;
    private String emailContent;

    public abstract String createEmailSubject();
    public abstract String createEmailContext();

}
