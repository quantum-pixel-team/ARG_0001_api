package com.quantum_pixel.arg.hotel.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotelBookBatchScheduler {
    private final JobLauncher jobLauncher;

    private final Job job;

    @Scheduled(cron = "* 1 * * * *")
    public void performBatchJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .addLong("run.id",1L)
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
