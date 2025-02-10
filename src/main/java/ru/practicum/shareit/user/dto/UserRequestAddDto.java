package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserRequestAddDto {
    int id;
    @NotNull
    String name;
    @Email(message = "Некорректный формат электронной почты")
    @NotNull(message = "Поле email - обязательное")
    String email;

    public User toUser() {
        return new User()
                .setId(id)
                .setName(name)
                .setEmail(email);
    }
}
