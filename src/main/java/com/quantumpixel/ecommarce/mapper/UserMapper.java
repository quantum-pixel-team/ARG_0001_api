package com.quantumpixel.ecommarce.mapper;

import com.quantumpixel.ecommarce.dto.UserResponseDto;
import com.quantumpixel.ecommarce.dto.UserUpdateDto;
import com.quantumpixel.ecommarce.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserUpdateDto userUpdateDto);

    UserResponseDto toDto(User user);

}