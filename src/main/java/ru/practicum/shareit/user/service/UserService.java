package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataConflictException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;

    }

    public UserDTO addUser(UserRequestAddDto user) {
        if (isNotUniqueEmail(user.getEmail())) {
            throw new DataConflictException("Пользователь с указанным email уже зарегистрирован");
        }
        return UserMapper.INSTANCE.toDTO(userStorage.saveUser(UserMapper.INSTANCE.toEntity(user)));
    }

    public UserDTO updateUser(UserRequestPatchDto user, int userId) {
        User curUser = userStorage.getUser(userId);
        if (user.getName() != null) {
            curUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (isNotUniqueEmail(user.getEmail())) {
                throw new DataConflictException("Невозможно обновить пользователя, указанный email уже используется");
            }
            curUser.setEmail(user.getEmail());
        }
        return UserMapper.INSTANCE.toDTO(userStorage.updateUser(curUser, userId));
    }

    public List<UserDTO> getUsersList() {
        return userStorage.getUsers().stream().map(UserMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUser(int id) {
        return UserMapper.INSTANCE.toDTO(userStorage.getUser(id));
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }


    private boolean isNotUniqueEmail(String email) {
            User user = userStorage.getUserByEmail(email);
            return Objects.nonNull(user);
    }
}
