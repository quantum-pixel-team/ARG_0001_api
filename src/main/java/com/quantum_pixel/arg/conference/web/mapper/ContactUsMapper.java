package com.quantum_pixel.arg.conference.web.mapper;

import com.quantum_pixel.arg.conference.model.ContactUsMailStructure;
import com.quantum_pixel.arg.conference.model.MailStructure;
import com.quantum_pixel.arg.v1.web.model.ConfernceMailStructureDTO;
import com.quantum_pixel.arg.v1.web.model.ContactUsMailStructureDTO;
import io.swagger.annotations.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContactUsMapper {

   ContactUsMailStructure toEntity(ContactUsMailStructureDTO contactUsMailStructureDTO);
}
