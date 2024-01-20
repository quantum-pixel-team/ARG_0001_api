package com.quantum_pixel.ecm.controller;

import com.quantum_pixel.ecm.service.UserService;
import com.quantum_pixel.ecm.v1.web.UsersApi;
import com.quantum_pixel.ecm.v1.web.model.CreateUserDTO;
import com.quantum_pixel.ecm.v1.web.model.UpdateUserRequestDTO;
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
    public ResponseEntity<List<UserDTO>> createUsers(List<CreateUserDTO> createUserDTO) {
        List<UserDTO> createdUserList = userService.createUsers(createUserDTO);
        return ResponseEntity.ok(createdUserList);
    }

    @Override
    public ResponseEntity<Void> deleteUsersById(List<Long> userIds) {
        userService.deleteUsersById(userIds);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<List<UserDTO>> updateUsers(List<UpdateUserRequestDTO> updateUserRequestDTOS) {
        var result = userService.updateUsers(updateUserRequestDTOS);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
