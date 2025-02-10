package ru.practicum.shareit.user.dto;

import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserRequestPatchDto {
    int id;
    String name;
    String email;

    public User toUser() {
        return new User()
                .setId(id)
                .setName(name)
                .setEmail(email);
    }
}
