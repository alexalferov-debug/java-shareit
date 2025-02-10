package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

@Getter
public class ItemRequestAddDto {
    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;
    @NotNull
    private Boolean available;
    private int ownerId;

    public Item toItem() {
        return new Item()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setAvailable(available);
    }
}
