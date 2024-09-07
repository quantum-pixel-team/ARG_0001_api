package com.quantum_pixel.arg.hotel.specification;


import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record RoomReservationSearch(Long roomId, LocalDateTime startDate, LocalDateTime endDate) {
    public Specification<RoomReservation> getSpecification() {
        var list = new ArrayList<Specification<RoomReservation>>(List.of());
        list.add(RoomReservationSpecification.findByRoomId(roomId));
        list.add(RoomReservationSpecification.isReservationDateBetween(startDate,endDate));
        return list.stream()
                .reduce(Specification::and)
                .get();

    }
}
