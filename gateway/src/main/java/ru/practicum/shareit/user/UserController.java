package ru.practicum.shareit.user;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {
    UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable @Positive Long id) {
        log.info("Get user with id {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get users");
        return userClient.getUserList();
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated UserRequestAddDto user) {
        log.info("Получен запрос на добавление пользователя с email {} ", user.getEmail());
        return userClient.createUser(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestPatchDto user, @PathVariable @Positive Long userId) {
        log.info("Получен запрос на обновление пользователя с id = {}", userId);
        return userClient.updateUser(userId, user);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive Long userId) {
        log.info("Получен запрос на удаление пользователя с id = {}", userId);
        return userClient.deleteUser(userId);
    }
}
