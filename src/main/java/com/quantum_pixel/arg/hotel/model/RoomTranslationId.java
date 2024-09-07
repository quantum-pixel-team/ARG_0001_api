package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RoomTranslationId implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -9027658685115834029L;
    @NotNull
    @Column(name = "room_id", nullable = false)
    private Integer roomId;

    @Size(max = 5)
    @NotNull
    @Column(name = "language", nullable = false, length = 5)
    private String language;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoomTranslationId entity = (RoomTranslationId) o;
        return Objects.equals(this.language, entity.language) &&
                Objects.equals(this.roomId, entity.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, roomId);
    }

}