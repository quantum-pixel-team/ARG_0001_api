package com.quantum_pixel.ecm.controller;

import com.quantum_pixel.ecm.service.UserService;
import com.quantum_pixel.ecm.v1.web.UsersApi;
import com.quantum_pixel.ecm.v1.web.model.CreateUserDTO;
import com.quantum_pixel.ecm.v1.web.model.EditUserRequestDTO;
import com.quantum_pixel.ecm.v1.web.model.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {


    private final UserService userService;


    @Override
    public ResponseEntity<UserDTO> createNewUser(CreateUserDTO createUserDTO) {
        log.info("[ENDPOINT] Received request to create a new user");
        var result = userService.createUser(createUserDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }

    @Override
    public ResponseEntity<Void> deleteUserById(Long id) {
        log.info("[ENDPOINT] Received request to delete scenario");
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public ResponseEntity<UserDTO> editUser(Long id, EditUserRequestDTO editUserRequestDTO) {
        var result = userService.editUser(id, editUserRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
