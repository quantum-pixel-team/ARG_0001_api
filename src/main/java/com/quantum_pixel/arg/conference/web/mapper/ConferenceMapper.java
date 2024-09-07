package com.quantum_pixel.arg.conference.web.mapper;

import com.quantum_pixel.arg.common.web.mapper.DateTimeMapper;
import com.quantum_pixel.arg.conference.model.ConferenceMailStructure;
import com.quantum_pixel.arg.v1.web.model.ConferenceMailStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConferenceMapper extends DateTimeMapper {

    ZoneId TIRANE_ZONE_ID = ZoneId.of("Europe/Tirane");

    ConferenceMailStructure toEntity(ConferenceMailStructureDTO conferenceMailStructureDTO);

    default LocalTime toLocalTime(OffsetDateTime value) {
        return value.atZoneSameInstant(TIRANE_ZONE_ID).toLocalTime();
    }

    default LocalDate toLocalDate(OffsetDateTime value) {
        return value.atZoneSameInstant(TIRANE_ZONE_ID).toLocalDate();
    }
}
