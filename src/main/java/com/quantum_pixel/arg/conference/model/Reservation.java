package com.quantum_pixel.arg.conference.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
@Builder
public class Reservation {
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Optional<Long> numberOfAttenders;

    @Override
    public String toString() {
        if (numberOfAttenders.isPresent()) {
            return
                    "\tData:" + reservationDate + "\n" +
                            "\tNumri i Pjesëmarrësve: " + numberOfAttenders.get() + "\n" +
                            "\tOra e Fillimit: " + startTime + "\n" +
                            "\tOra e Mbarimit:" + endTime + "\n"
                    ;
        } else {
            return "\tData:" + reservationDate + "\n" +
                    "\tOra e Fillimit: " + startTime + "\n" +
                    "\tOra e Mbarimit:" + endTime + "\n"
                    ;
        }


    }
}
