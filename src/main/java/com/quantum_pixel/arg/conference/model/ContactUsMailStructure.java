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
        return " Njoftim për mesazh nga Contuct Us";
    }

    @Override
    public String createEmailContext() {
        return "Përshëndetje,\n" +
                "\n" +
                "Ju njoftojmë se keni marrë një mesazh me detajet e mëposhtme:\n" +
                "Emri: " + getFullNameOrCompanyName() + " ,\n" +
                "Email: " + email + " ,\n" +
                "Mesazhi: " + message + "\n"
                + " Ju lutemi, përdorni informacionin e dhënë më sipër për të kontaktuar klientin.\n" +
                "\n" +
                "Ju lutemi mos i bëni reply këtij emaili.\n" +
                "\n" +
                "Faleminderit.";
    }
}
