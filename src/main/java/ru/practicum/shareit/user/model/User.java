package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {
    int id;
    String name;
    String email;
}
