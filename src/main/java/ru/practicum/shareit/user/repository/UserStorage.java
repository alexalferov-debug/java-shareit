package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(int userId);

    List<User> getUsers();

    User saveUser(User user);

    User updateUser(User user, int userId);

    void deleteUser(int id);
}
