package com.quantum_pixel.arg.hotel.model;

import java.util.List;

public interface RoomView {
    Long getId();
    String getName();
    String getDescription();
    String getShortDescription();
    Float getTotalPrice();
    Integer getTotalCapacity();
    List<String> getImagesUrl();
    Integer getAvailableRooms();
    Integer getMinimumNights();
    String getFacilities();
}

