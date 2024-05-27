package com.quantum_pixel.arg.conference.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@RequiredArgsConstructor
@Data
@ToString
@SuperBuilder
public abstract class MailStructure {
    private String fullNameOrCompanyName;


    public abstract String createEmailSubject();
    public abstract String createEmailContext();

}
