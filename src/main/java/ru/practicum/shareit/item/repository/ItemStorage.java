package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item getItem(int id);

    List<Item> getAllItemsByUserId(int userId);

    Item addItem(Item item, int userId);

    Item updateItem(Item item, int itemId, int userId);

    List<Item> searchItem(String text);
}
