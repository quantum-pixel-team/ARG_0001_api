package com.quantum_pixel.arg.hotel.web.mapper;

import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.model.dao.RoomReservationDao;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HotelRoomMapper {

    List<RoomDTO> toDTOList(List<Room> reservationDetails);

    @Mapping(target = "roomId", source = "id.roomId")
    @Mapping(target = "date", source = "id.date")
    RoomReservationDTO toRoomReservationDTO(RoomReservation roomReservation);

    List<Room> toEntity(List<RoomDao> reservationDetails);

    @Mapping(target = "id.roomId", source = "roomId")
    @Mapping(target = "id.date", source = "date")
    RoomReservation toEntity(RoomReservationDao roomReservationDao);
}
