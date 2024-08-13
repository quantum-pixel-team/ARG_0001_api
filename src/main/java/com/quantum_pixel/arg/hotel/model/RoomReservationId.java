package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;
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
    private LocalDateTime date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomReservationId that)) return false;
        return Objects.equals(getRoomId(), that.getRoomId()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomId(), getDate());
    }
}