package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

@Getter
@AllArgsConstructor
public class ItemRequestPatchDto {
    private String name;
    private String description;
    private Boolean available;
}
