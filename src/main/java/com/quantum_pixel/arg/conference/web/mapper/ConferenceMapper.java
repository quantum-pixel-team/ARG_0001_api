package com.quantum_pixel.arg.conference.web.mapper;

import com.quantum_pixel.arg.conference.model.ConferenceMailStructure;
import com.quantum_pixel.arg.v1.web.model.ConfernceMailStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConferenceMapper {
    ConferenceMailStructure toEntity(ConfernceMailStructureDTO confernceMailStructureDTO);

}
