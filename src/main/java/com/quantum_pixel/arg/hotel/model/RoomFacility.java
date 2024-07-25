package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "room_facility")
public class RoomFacility {
    @EmbeddedId
    private RoomFacilityId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @MapsId("facilityId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

}