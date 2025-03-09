package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserRequestPatchDto user, @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(userService.updateUser(user, userId));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserRequestAddDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsersList() {
        return ResponseEntity.ok(userService.getUsersList());
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
