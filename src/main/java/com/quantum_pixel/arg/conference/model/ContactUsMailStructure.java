package com.quantum_pixel.arg.conference.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@Data
@SuperBuilder
public class ContactUsMailStructure extends MailStructure {
    private String email;
    private String message;
    @Override
    public String createEmailSubject() {
        return "Contact Us Request from client";
    }

    @Override
    public String createEmailContext() {
        return "Client Full Name: " + getFullNameOrCompanyName()+ " ," +
                "Client Email: " + email + " ," +
                "Client Message: " + message
                + " Thank you for reading the message";
    }
}
