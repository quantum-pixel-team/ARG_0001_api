package com.quantum_pixel.arg.event.web.mapper;

import com.quantum_pixel.arg.common.web.mapper.DateTimeMapper;
import com.quantum_pixel.arg.event.model.Event;
import com.quantum_pixel.arg.v1.web.model.EventDTO;
import com.quantum_pixel.arg.v1.web.model.PageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.time.*;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper extends DateTimeMapper {
    Event toEntity(EventDTO eventDTO);



    default LocalDate toLocalDate(Optional<OffsetDateTime> value) {
        return value.map(el -> el.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate()).orElse(null);
    }
    default LocalDate toLocalDate(OffsetDateTime value) {
        return value.withOffsetSameInstant(ZoneOffset.UTC).toLocalDate();
    }
    PageDTO toPageDto(Page<Event> result);
}
