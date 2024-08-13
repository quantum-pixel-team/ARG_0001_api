package com.quantum_pixel.arg.hotel.model.app_in_connect;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateDate {
    private LocalDateTime date;
    private boolean available;
    private int avail;
    private int minStay;
    private int guests;
    private int capacity;
    private String rate;
}
