package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Getter
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
}
