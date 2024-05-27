package com.quantum_pixel.arg.conference.web.mapper;

import com.quantum_pixel.arg.conference.model.ContactUsMailStructure;
import com.quantum_pixel.arg.v1.web.model.ContactUsMailStructureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactUsMapper {

   ContactUsMailStructure toEntity(ContactUsMailStructureDTO contactUsMailStructureDTO);
}
