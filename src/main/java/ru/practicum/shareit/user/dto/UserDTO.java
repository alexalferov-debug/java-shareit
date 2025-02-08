package ru.practicum.shareit.user.dto;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserDTO {
    private final int id;
    private final String name;
    private final String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
