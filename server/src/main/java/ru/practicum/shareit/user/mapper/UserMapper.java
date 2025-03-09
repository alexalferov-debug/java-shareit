package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.model.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    UserRequestAddDto toRequestAddDTO(User user);

    UserRequestPatchDto toRequestPatchDTO(User user);

    User toEntity(UserRequestAddDto addDto);

    User toEntity(UserDTO addDto);

    User toEntity(UserRequestPatchDto addDto);
}
