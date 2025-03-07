package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(int id, int userId);

    ItemWithoutOwnerDto createItem(ItemRequestAddDto item, int userId);

    List<ItemWithoutOwnerDto> getAllItems(int userId);

    ItemWithoutOwnerDto updateItem(ItemRequestPatchDto item, int itemId, int userId);

    List<ItemWithoutOwnerDto> searchItem(String text);

    CommentDto addComment(AddCommentDto comment, int itemId, int userId);
}
