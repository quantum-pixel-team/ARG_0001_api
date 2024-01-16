package com.quantum_pixel.ecm.service;

import com.quantum_pixel.ecm.mapper.UserMapper;
import com.quantum_pixel.ecm.model.User;
import com.quantum_pixel.ecm.repository.UserRepository;
import com.quantum_pixel.ecm.v1.web.model.CreateUserDTO;
import com.quantum_pixel.ecm.v1.web.model.EditUserRequestDTO;
import com.quantum_pixel.ecm.v1.web.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserDTO getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User cannot be found id: %d".formatted(id)));
        return mapper.toDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }


    public UserDTO createOrUpdateUser(EditUserRequestDTO userUpdateDto) {
        User entity = mapper.toEntity(userUpdateDto);
        return mapper.toDto(repository.save(entity));
    }

    public List<UserDTO> createOrUpdateOrDeleteUsers(EditUserRequestDTO[] userUpdateDto) {
        var entities = Arrays.stream(userUpdateDto).map(mapper::toEntity).toList();
        List<Long> userIds = entities.stream().map(User::getId)
                .filter(Objects::nonNull)
                .toList();

        repository.findAll().stream().map(User::getId)
                .filter(el -> !userIds.contains(el))
                .forEach(repository::deleteById);

        return entities.stream()
                .map(repository::save)
                .map(mapper::toDto)
                .toList();
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    public UserDTO createUser(CreateUserDTO userDTO) {
        User user = mapper.toEntity(userDTO);
        return mapper.toDto(repository.save(user));
    }

    public UserDTO editUser(Long id, EditUserRequestDTO userRequestDTO) {
        User user = mapper.toEntity(userRequestDTO);
        user.setId(id);
        return repository.findById(id)
                .map(el -> user)
                .map(repository::save)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User cannot be found id: %d".formatted(id)));

    }
}
