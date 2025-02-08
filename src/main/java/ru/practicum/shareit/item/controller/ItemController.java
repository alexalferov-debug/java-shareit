package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemService;

    @Autowired
    public ItemController(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestBody @Valid ItemRequestAddDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> partialUpdate(@PathVariable("id") @Positive int id, @RequestBody ItemRequestPatchDto item, @RequestHeader(value = "X-Sharer-User-Id") @Positive int userId) {
        return ResponseEntity.ok(itemService.updateItem(item, id, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> get(@PathVariable("id") @Positive int id) {
        return ResponseEntity.ok(itemService.getItem(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllByUserId(@RequestHeader(value = "X-Sharer-User-Id") int userId) {
        return ResponseEntity.ok(itemService.getAllItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestParam(name = "text") String query) {
        return ResponseEntity.ok(itemService.searchItem(query));
    }
}
