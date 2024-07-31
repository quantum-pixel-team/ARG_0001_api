package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_translation")
public class RoomTranslation {
    @EmbeddedId
    private RoomTranslationId id;

    @MapsId("roomId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

}