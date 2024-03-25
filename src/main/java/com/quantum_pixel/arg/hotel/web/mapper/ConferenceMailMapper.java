package com.quantum_pixel.arg.hotel.web.mapper;

import com.quantum_pixel.arg.hotel.model.mail.ConferenceMailStructure;
import com.quantum_pixel.arg.v1.web.model.ConfernceMailStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConferenceMailMapper {
    ConferenceMailStructure toObject(ConfernceMailStructureDTO confernceMailStructureDTO);

}
