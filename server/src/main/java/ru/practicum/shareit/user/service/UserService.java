package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;

    }

    public UserDTO addUser(UserRequestAddDto user) {
        try {
            User newUser = UserMapper.INSTANCE.toEntity(user);
            User savedUser = userStorage.saveUser(newUser);
            return UserMapper.INSTANCE.toDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataConflictException("Пользователь с указанным email уже зарегистрирован");
        }
    }


    public UserDTO updateUser(UserRequestPatchDto user, Long userId) {
        User curUser = userStorage.getUser(userId);
        if (user.getName() != null) {
            curUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            curUser.setEmail(user.getEmail());
        }
        try {
            return UserMapper.INSTANCE.toDTO(userStorage.updateUser(curUser, userId));
        } catch (DataIntegrityViolationException e) {
            throw new DataConflictException("Пользователь с указанным email уже зарегистрирован");
        }
    }

    public List<UserDTO> getUsersList() {
        return userStorage.getUsers().stream().map(UserMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUser(Long id) {
        return UserMapper.INSTANCE.toDTO(userStorage.getUser(id));
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
