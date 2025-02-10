package ru.practicum.shareit.item.dto;

import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

/**
 * TODO Sprint add-controllers.
 */
@Getter
public class ItemDto {
    private final int id;
    private final String name;
    private final String description;
    private final Boolean available;

    public ItemDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
    }
}
