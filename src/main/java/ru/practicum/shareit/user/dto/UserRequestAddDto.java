package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Getter
@AllArgsConstructor
public class UserRequestAddDto {
    Integer id;
    @NotNull
    String name;
    @Email(message = "Некорректный формат электронной почты")
    @NotNull(message = "Поле email - обязательное")
    String email;
}
