package ru.practicum.shareit.item.dto;

import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

@Getter
public class ItemRequestPatchDto {
    private String name;
    private String description;
    private Boolean available;

    public Item toItem() {
        return new Item()
                .setName(name)
                .setDescription(description)
                .setAvailable(available);
    }
}
