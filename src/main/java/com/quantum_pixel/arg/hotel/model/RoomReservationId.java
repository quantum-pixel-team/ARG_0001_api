package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomReservationId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -4858119348541539600L;
    @NotNull
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoomReservationId entity = (RoomReservationId) o;
        return Objects.equals(this.date, entity.date) &&
                Objects.equals(this.roomId, entity.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, roomId);
    }

}