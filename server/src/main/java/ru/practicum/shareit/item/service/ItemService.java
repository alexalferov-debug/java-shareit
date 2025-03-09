package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.List;

public interface ItemService {
    ItemDto getItem(Long id, Long userId);

    ItemWithoutOwnerDto createItem(ItemRequestAddDto item, Long userId);

    List<ItemWithoutOwnerDto> getAllItems(Long userId);

    ItemWithoutOwnerDto updateItem(ItemRequestPatchDto item, Long itemId, Long userId);

    List<ItemWithoutOwnerDto> searchItem(String text);

    CommentDto addComment(AddCommentDto comment, Long itemId, Long userId);
}
