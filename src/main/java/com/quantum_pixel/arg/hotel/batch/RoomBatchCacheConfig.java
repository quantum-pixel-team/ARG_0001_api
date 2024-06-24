package com.quantum_pixel.arg.hotel.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.repository.RoomReservationRepository;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
@Configuration
@RequiredArgsConstructor
public class RoomBatchCacheConfig {

    private final RoomReservationRepository roomReservationRepository;

    private final CostumeSkipPolicy costumeSkipPolicy;

    @Bean
    public Step step1(JobRepository jobRepository,  PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<RoomReservation,RoomReservation>chunk(2,platformTransactionManager)
                .reader(roomReservationReader())
                .writer(itemWriter())
                .faultTolerant()
                .skipLimit(10)
                .skipPolicy(costumeSkipPolicy)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("first Test", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public ListItemReader<RoomReservation> roomReservationReader(){
        return new ListItemReader<>(roomsReservation());
    }


    @SneakyThrows
    private List<RoomReservation> roomsReservation(){
        ClassPathResource staticDataResource = new ClassPathResource("test_data/room-reservation.json");
        String string = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        ObjectMapper mapper= JsonMapper.builder().addModule(new JavaTimeModule()).build();
        RoomReservation[] roomPrototypes = mapper.readValue(string, RoomReservation[].class);
        return Arrays.asList(roomPrototypes);
    }

    @Bean
    public RepositoryItemWriter<RoomReservation> itemWriter(){
        RepositoryItemWriter<RoomReservation> reservationRepositoryItemWriter=new RepositoryItemWriter<>();
        reservationRepositoryItemWriter.setRepository(roomReservationRepository);
        return reservationRepositoryItemWriter;
    }

}
