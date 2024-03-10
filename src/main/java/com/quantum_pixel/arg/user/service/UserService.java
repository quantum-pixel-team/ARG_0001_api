package com.quantum_pixel.arg.user.service;

import com.quantum_pixel.arg.user.web.mapper.UserMapper;
import com.quantum_pixel.arg.user.model.User;
import com.quantum_pixel.arg.user.repository.UserRepository;
import com.quantum_pixel.arg.v1.web.model.CreateUserDTO;
import com.quantum_pixel.arg.v1.web.model.UpdateUserRequestDTO;
import com.quantum_pixel.arg.v1.web.model.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }


    public UserDTO updateUser(Long id, UpdateUserRequestDTO userRequestDTO) {
        User user = mapper.toEntity(userRequestDTO);
        user.setId(id);
        return repository.findById(id)
                .map(el -> user)
                .map(repository::save)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User cannot be found id: %d".formatted(id)));

    }

    public List<UserDTO> updateUsers(List<UpdateUserRequestDTO> editUserRequestDTO) {
        return editUserRequestDTO.stream().map(el -> updateUser(el.getId(), el))
                .toList();
    }

    public List<UserDTO> createUsers(List<CreateUserDTO> createUserDTO) {
        List<User> users = createUserDTO.stream().map(mapper::toEntity).toList();
        return repository.saveAll(users).stream().map(mapper::toDto).toList();
    }

    public void deleteUsersById(List<Long> userIds) {
        userIds.forEach(repository::deleteById);
    }
}
