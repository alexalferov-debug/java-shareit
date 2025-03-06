package ru.practicum.shareit.item.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemWithoutOwnerDto> create(@RequestBody @Validated ItemRequestAddDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestBody @Validated AddCommentDto addCommentDto, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId, @PathVariable @Positive Integer itemId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addComment(addCommentDto, itemId, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemWithoutOwnerDto> partialUpdate(@PathVariable("id") @Positive int id, @RequestBody @Validated ItemRequestPatchDto item, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId) {
        return ResponseEntity.ok(itemService.updateItem(item, id, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemWithoutOwnerDto> get(@PathVariable("id") @Positive int id, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId) {
        return ResponseEntity.ok(ItemMapper.INSTANCE.toItemWithoutOwnerDto(itemService.getItem(id, userId)));
    }

    @GetMapping
    public ResponseEntity<List<ItemWithoutOwnerDto>> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemWithoutOwnerDto>> search(@RequestParam(name = "text") String query) {
        return ResponseEntity.ok(itemService.searchItem(query));
    }
}
