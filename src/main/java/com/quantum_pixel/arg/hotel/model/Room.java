package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "rate_applies_to")
    private Integer rateAppliesTo;

    @Column(name = "priority")
    private Integer priority;

    @ManyToMany
    @JoinTable(name = "room_facility",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id"))

    private Set<Facility> facilities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "room")
    private Set<RoomReservation> roomReservations = new LinkedHashSet<>();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "images_url", columnDefinition = "text[]")
    private String[] imagesUrl;

}
