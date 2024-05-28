package com.quantum_pixel.arg.conference.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Reservation {
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return
                "\tDate:" + reservationDate + "\n" +
                "\tOra e fillimit:" + startTime + "\n"+
                "\tOra E mbarimit:" + endTime +"\n"
                ;
    }
}
