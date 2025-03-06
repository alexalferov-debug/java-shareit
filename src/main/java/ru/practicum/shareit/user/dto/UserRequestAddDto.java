package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
