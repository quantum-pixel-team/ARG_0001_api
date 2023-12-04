package com.quantumpixel.ecommarce.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.quantumpixel.ecommarce.model.User}
 */
@Value
public class UserResponseDto implements Serializable {
    Long id;
    @Size(max = 30)
    String firstName;
    @Size(max = 30)
    String lastName;
}