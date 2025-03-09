package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> getItem(Long id);

    List<Item> getAllItemsByUserId(Long userId);

    Item addItem(Item item, Long userId);

    Item updateItem(Item item, Long itemId, Long userId);

    List<Item> searchItem(String text);

    Comment addComment(Comment comment);
}
