package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(Long userId);

    List<User> getUsers();

    User saveUser(User user);

    User updateUser(User user, Long userId);

    void deleteUser(Long id);

    User getUserByEmail(String email);
}
