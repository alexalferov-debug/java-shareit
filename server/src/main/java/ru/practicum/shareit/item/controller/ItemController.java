package ru.practicum.shareit.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemWithoutOwnerDto> create(@RequestBody ItemRequestAddDto itemDto, @RequestHeader(value = USER_HEADER) Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto, userId));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestBody AddCommentDto addCommentDto, @RequestHeader(value = USER_HEADER) Long userId, @PathVariable Long itemId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.addComment(addCommentDto, itemId, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemWithoutOwnerDto> partialUpdate(@PathVariable("id") Long id, @RequestBody ItemRequestPatchDto item, @RequestHeader(value = USER_HEADER) Long userId) {
        return ResponseEntity.ok(itemService.updateItem(item, id, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemWithoutOwnerDto> get(@PathVariable("id") Long id, @RequestHeader(value = USER_HEADER) Long userId) {
        return ResponseEntity.ok(ItemMapper.INSTANCE.toItemWithoutOwnerDto(itemService.getItem(id, userId)));
    }

    @GetMapping
    public ResponseEntity<List<ItemWithoutOwnerDto>> getAllByUserId(@RequestHeader(value = USER_HEADER) Long userId) {
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemWithoutOwnerDto>> search(@RequestParam(name = "text") String query) {
        return ResponseEntity.ok(itemService.searchItem(query));
    }
}
