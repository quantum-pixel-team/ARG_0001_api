package com.quantum_pixel.arg.hotel.batch;

import com.quantum_pixel.arg.hotel.service.HotelBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class RoomBatchCacheConfig {


    private final HotelBookingService service;

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
                    service.triggerRoomReservationUpdate(OffsetDateTime.now(),OffsetDateTime.now().plusMonths(12),
                            Optional.empty());
                    return RepeatStatus.FINISHED;
                },platformTransactionManager).build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("first Test", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }



}
