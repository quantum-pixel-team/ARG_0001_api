package com.quantumpixel.ecommarce.controller;

import com.quantumpixel.ecommarce.dto.UserResponseDto;
import com.quantumpixel.ecommarce.dto.UserUpdateDto;
import com.quantumpixel.ecommarce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService service;

    @GetMapping("users/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("users")
    public List<UserResponseDto> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("users")
    public UserResponseDto createOrUpdateUser(@RequestBody UserUpdateDto userUpdateDto) {

        return service.createOrUpdateUser(userUpdateDto);
    }

}
