package com.quantum_pixel.arg.hotel.model.app_in_connect;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rate {
    private Long rateId;
    private List<RateDate> dates;
}

