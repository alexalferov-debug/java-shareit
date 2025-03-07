package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequestAddDto {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;
    @NotNull
    private Boolean available;
    private int ownerId;
}
