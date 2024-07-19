package com.quantum_pixel.arg.conference.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.stereotype.Component;

@EnableIntegration
@Component
public class MailIntegrationFlow {
    @Bean
    public IntegrationFlow imapMailFlow(@Value("imaps://${IMAP_USERNAME}:${IMAP_PASSWORD}@${IMAP_HOST}:${IMAP_PORT}/inbox") String storeUrl) {
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

    @Bean
    public IntegrationFlow processImapMessages() {
        return IntegrationFlow.from("imapIdleChannel")
                .aggregate(aggregatorSpec -> aggregatorSpec.correlationStrategy(message -> 1)
                        .releaseStrategy(group -> group.size()>=5)
                        .groupTimeout(5000L)
                        .sendPartialResultOnExpiry(true))
                .channel(MessageChannels.queue("aggregateChannel"))
                .get();
    }

    @Bean
    public IntegrationFlow processAggregatedMessages() {
        return IntegrationFlow.from("aggregateChannel")
                .handle(message -> System.out.println(message.getPayload()+ "some" + message.getHeaders().getTimestamp()))
                .get();
    }
}
