package com.quantum_pixel.arg.hotel.specification;


import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;

public record RoomReservationSearch(Long roomId, LocalDate startDate, LocalDate endDate) {
    public Specification<RoomReservation> getSpecification() {
        var list = new ArrayList<Specification<RoomReservation>>();
        list.add(RoomReservationSpecification.findByRoomId(roomId));
        list.add(RoomReservationSpecification.isReservationDateBetween(startDate,endDate));
        return list.stream()
                .reduce(Specification::and)
                .get();

    }
}
