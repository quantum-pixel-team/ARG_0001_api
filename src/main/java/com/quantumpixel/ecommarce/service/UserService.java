package com.quantumpixel.ecommarce.service;

import com.quantumpixel.ecommarce.dto.UserResponseDto;
import com.quantumpixel.ecommarce.dto.UserUpdateDto;
import com.quantumpixel.ecommarce.mapper.UserMapper;
import com.quantumpixel.ecommarce.model.User;
import com.quantumpixel.ecommarce.repository.UserRepository;
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

    public UserResponseDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User cannot be found id: %d".formatted(id)));
        return mapper.toDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }


    public UserResponseDto createOrUpdateUser(UserUpdateDto userUpdateDto) {
        User entity = mapper.toEntity(userUpdateDto);
        return mapper.toDto(repository.save(entity));
    }
}
