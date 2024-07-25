package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RoomFacilityId implements java.io.Serializable {
    private static final long serialVersionUID = 3369146139535958569L;
    @NotNull
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @NotNull
    @Column(name = "facility_id", nullable = false)
    private Integer facilityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoomFacilityId entity = (RoomFacilityId) o;
        return Objects.equals(this.facilityId, entity.facilityId) &&
                Objects.equals(this.roomId, entity.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilityId, roomId);
    }

}