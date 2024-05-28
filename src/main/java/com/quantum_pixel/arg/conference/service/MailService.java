package com.quantum_pixel.arg.conference.service;

import com.quantum_pixel.arg.conference.model.MailStructure;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MailService {
    private static final String email = "lukabuziu42@gmail.com";

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String EMAIL;

    @Bean
    public  IntegrationFlow imapMailFlow(@Value("imaps://${IMAP_USERNAME}:${IMAP_PASSWORD}@${IMAP_HOST}:${IMAP_PORT}/inbox") String storeUrl) {
        return IntegrationFlow
                .from(Mail.imapIdleAdapter(storeUrl)
                        .autoStartup(true)
                        .shouldReconnectAutomatically(true)
                        .shouldDeleteMessages(false)
                        .autoCloseFolder(false))
                .channel(MessageChannels.queue("imapIdleChannel"))
                .log()
                .get();
    }

    @SneakyThrows
    public void sendEmail(MailStructure mailStructure) {
        createSimpleEmail(mailStructure);
    }

    private void createSimpleEmail(MailStructure mailStructure) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(mailStructure.createEmailSubject());
        simpleMailMessage.setFrom(EMAIL);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(mailStructure.createEmailContext());
        javaMailSender.send(simpleMailMessage);
    }



}
