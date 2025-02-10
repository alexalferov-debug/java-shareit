package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id);

    ItemDto createItem(ItemRequestAddDto item, int userId);

    List<ItemDto> getAllItems(int userId);

    ItemDto updateItem(ItemRequestPatchDto item, int itemId, int userId);

    List<ItemDto> searchItem(String text);
}
