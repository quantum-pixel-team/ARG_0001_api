package com.quantum_pixel.arg.hotel.web.mapper;

import com.quantum_pixel.arg.hotel.model.RoomPrototype;
import com.quantum_pixel.arg.v1.web.model.RoomPrototypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelRoomMapper {

    List<RoomPrototypeDTO> toDTOList(List<RoomPrototype> reservationDetails);
}
