package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDTO addUser(UserRequestAddDto user) {
        return new UserDTO(userStorage.saveUser(user.toUser()));
    }

    public UserDTO updateUser(UserRequestPatchDto user, int userId) {
        return new UserDTO(userStorage.updateUser(user.toUser(), userId));
    }

    public List<UserDTO> getUsersList() {
        return userStorage.getUsers().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public UserDTO getUser(int id) {
        return new UserDTO(userStorage.getUser(id));
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }
}
