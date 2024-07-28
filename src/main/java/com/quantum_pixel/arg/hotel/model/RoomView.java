package com.quantum_pixel.arg.hotel.model;

import java.util.List;

public interface RoomView {
    Long getId();
    String getName();
    String getDescription();
    Float getTotalPrice();
    Integer getCapacity();
    List<String> getImagesUrl();

    String getFacilities();
}

