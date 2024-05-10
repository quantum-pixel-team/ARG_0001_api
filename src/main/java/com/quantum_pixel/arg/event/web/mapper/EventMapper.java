package com.quantum_pixel.arg.event.web.mapper;

import com.quantum_pixel.arg.event.model.Event;
import com.quantum_pixel.arg.v1.web.model.EventDTO;
import com.quantum_pixel.arg.v1.web.model.PageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    Event toEntity(EventDTO eventDTO);

    default LocalDate toDate(Optional<LocalDate> value) {
        return value.orElse(null);
    }

    default LocalTime toLocalTime(Optional<LocalTime> value) {
        return value.orElse(null);
    }

    PageDTO toPageDto(Page<Event> result);
}
