package com.quantum_pixel.arg.hotel.model.app_in_connect;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDate {
    private LocalDate date;
    private boolean available;
    private int avail;
    private int minStay;
    private int guests;
    private int capacity;
    private String rate;
}
