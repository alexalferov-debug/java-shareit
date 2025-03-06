package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Getter
@AllArgsConstructor
public class UserRequestPatchDto {
    Integer id;
    String name;
    String email;
}
