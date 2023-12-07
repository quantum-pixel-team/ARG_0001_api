package com.quantumpixel.ecommarce.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.quantumpixel.ecommarce.model.User}
 */
@Value
public class UserCreateDto implements Serializable {
    @Size(max = 30)
    String firstName;
    @Size(max = 30)
    String lastName;
}