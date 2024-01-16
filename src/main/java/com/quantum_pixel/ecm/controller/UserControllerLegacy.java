package com.quantum_pixel.ecm.controller;

import com.quantum_pixel.ecm.service.UserService;
import com.quantum_pixel.ecm.v1.web.model.EditUserRequestDTO;
import com.quantum_pixel.ecm.v1.web.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserControllerLegacy {

    private final UserService service;

    @GetMapping("users/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return service.getUserById(id);
    }

    @GetMapping("users")
    public List<UserDTO> getAllUsers() {
        return service.getAllUsers();
    }

    @PutMapping("users")
    public List<UserDTO> createOrUpdateOrDeleteUsers(@RequestBody EditUserRequestDTO[] userUpdateDto) {
        return service.createOrUpdateOrDeleteUsers(userUpdateDto);
    }
    @PutMapping("user")
    public UserDTO createOrUpdateUser(@RequestBody EditUserRequestDTO userUpdateDto) {
        return service.createOrUpdateUser(userUpdateDto);
    }
    @DeleteMapping("users/{id}")
    public void deleteUser( @PathVariable Long id){
        service.deleteUserById(id);
    }

}
