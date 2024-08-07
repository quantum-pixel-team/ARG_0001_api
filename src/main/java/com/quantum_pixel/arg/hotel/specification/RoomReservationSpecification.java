package com.quantum_pixel.arg.hotel.specification;

import com.quantum_pixel.arg.hotel.model.Room;
import com.quantum_pixel.arg.hotel.model.RoomReservation;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class RoomReservationSpecification {
    private RoomReservationSpecification() {
    }

    private static final String ID = "id";
    private static final String ROOM_ID = "roomId";
    private static final String DATE = "date";


    public static Specification<RoomReservation> findByRoomId(Long roomId) {
        return (root, query, cb) -> cb.equal(root.get(ID).get(ROOM_ID), roomId);
    }

    public static Specification<RoomReservation> isReservationDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) ->
                cb.between(root.get(ID).get(DATE), startDate, endDate);
    }
}
