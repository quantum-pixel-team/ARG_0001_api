package com.quantum_pixel.arg.hotel.web.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import com.quantum_pixel.arg.hotel.model.RoomView;
import com.quantum_pixel.arg.hotel.model.dao.RoomDao;
import com.quantum_pixel.arg.hotel.model.dao.RoomReservationDao;
import com.quantum_pixel.arg.v1.web.model.PaginatedRoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomDTO;
import com.quantum_pixel.arg.v1.web.model.RoomFacilityDTO;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class HotelRoomMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public abstract List<RoomReservation> toReservationEntities(List<RoomReservationDao> reservationDetails);

    @Mapping(target = "id.roomId", source = "roomId")
    @Mapping(target = "id.date", source = "date")
    public abstract RoomReservation toEntity(RoomReservationDao roomReservationDao);

    public abstract PaginatedRoomDTO toPageDto(Page<RoomView> result);

    @SneakyThrows
    public List<RoomFacilityDTO> toFacilityDto(String jsonFacilities) {
        return objectMapper.readValue(jsonFacilities, new TypeReference<>() {
        });
    }

    public abstract List<Room> toRoomEntities(List<RoomDao> rooms);


    public abstract List<RoomDTO> toDto(List<RoomView> rooms2);
}
