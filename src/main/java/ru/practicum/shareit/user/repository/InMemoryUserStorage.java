package ru.practicum.shareit.user.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserStorage.class);


    @Override
    public User getUser(int userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("User not found: {}", userId);
                    return new NotFoundException("User not found with id: " + userId);
                });
    }

    @Override
    public List<User> getUsers() {
        logger.info("Получение списка пользователей");
        return users;
    }

    @Override
    public User saveUser(User user) {
        logger.debug("Попытка добавления пользователя: {}", user);
        user.setId(generateId());
        users.add(user);
        logger.info("Пользователь добавлен с ID: {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user, int userId) {
        logger.debug("Попытка обновления пользователя с ID: {}", userId);
        User curUser = getUser(userId);
        curUser.setName(user.getName());
        curUser.setEmail(user.getEmail());
        logger.info("Пользователь с ID {} обновлён", userId);
        return curUser;
    }

    @Override
    public void deleteUser(int id) {
        getUser(id);
        users.removeIf(u -> u.getId() == id);
    }

    public User getUserByEmail(String email) {
        return users
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    private int generateId() {
        return idGenerator.incrementAndGet();
    }
}
