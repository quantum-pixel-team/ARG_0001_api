package com.quantum_pixel.arg.conference.integration;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.SearchTerm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Optional;

@EnableIntegration
@Component
@RequiredArgsConstructor
@Slf4j
public class MailIntegrationFlow {
    private final HotelBookingService hotelBookingService;
    private final Clock clock;

    @Bean
    public IntegrationFlow imapMailFlow(@Value("imaps://${IMAP_USERNAME}:${IMAP_PASSWORD}@${IMAP_HOST}:${IMAP_PORT}/inbox") String storeUrl) {
       log.info("[MailIntegrationFlow]  initialing ");
        return IntegrationFlow
                .from(Mail.imapIdleAdapter(storeUrl)
                        .searchTermStrategy(this::unreadEmails)
                        .autoStartup(true)
                        .shouldReconnectAutomatically(true)
                        .shouldDeleteMessages(false)
                        .autoCloseFolder(false))
                .channel(MessageChannels.queue("imapIdleChannel"))
                .log()
                .get();
    }

    private SearchTerm unreadEmails(Flags supportedFlags, Folder folder) {
        return new FlagTerm(new Flags(Flags.Flag.SEEN), false);
    }

    @Bean
    public IntegrationFlow processImapMessages() {
        log.info("[MailIntegrationFlow]  grouping  ");
        return IntegrationFlow.from("imapIdleChannel")
                .aggregate(aggregatorSpec -> aggregatorSpec.correlationStrategy(message -> 1)
                        .releaseStrategy(group -> group.size() >= 10)
                        .groupTimeout(10_000L)
                        .sendPartialResultOnExpiry(true))
                .channel(MessageChannels.queue("aggregateChannel"))

                .get();
    }

    @Bean
    public IntegrationFlow processAggregatedMessages() {
        log.info("[MailIntegrationFlow]  executing  ");
        return IntegrationFlow.from("aggregateChannel")
                .handle(message -> this.hotelBookingService
                        .triggerRoomReservationUpdate(OffsetDateTime.now(clock), OffsetDateTime.now(clock).plusMonths(3L), Optional.empty()))
                .get();
    }
}
