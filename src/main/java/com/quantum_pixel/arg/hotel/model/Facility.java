package com.quantum_pixel.arg.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "facility")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facility_id_gen")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @ColumnDefault("0")
    @Column(name = "usage_count")
    private Integer usageCount;

}