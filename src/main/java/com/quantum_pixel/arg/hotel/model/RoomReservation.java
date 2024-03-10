package com.quantum_pixel.arg.hotel.model;

import java.time.LocalDate;

public record RoomReservation(String name, LocalDate date, Boolean sold, Integer currentPrice, Integer roomPrice, String checkoutUrl){}
