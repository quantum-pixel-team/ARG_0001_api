package com.quantum_pixel.arg.hotel.model.app_in_connect;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RateDate {
    private LocalDate date;
    private boolean available;
    private int avail;
    private int minStay;
    private int guests;
    private int capacity;
    private String rate;
}
