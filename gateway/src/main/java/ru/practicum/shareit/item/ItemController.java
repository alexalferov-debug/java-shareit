package ru.practicum.shareit.item;


import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemService) {
        this.itemClient = itemService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Validated ItemRequestAddDto itemDto, @RequestHeader(value = USER_HEADER) @Positive Long userId) {
        return itemClient.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Validated AddCommentDto addCommentDto, @RequestHeader(value = USER_HEADER) @Positive Long userId, @PathVariable @Positive Long itemId) {
        return itemClient.addComment(addCommentDto, userId, itemId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> partialUpdate(@PathVariable("id") @Positive Long id, @RequestBody @Validated ItemRequestPatchDto item, @RequestHeader(value = USER_HEADER) @Positive Long userId) {
        return itemClient.update(item, id, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") @Positive Long id, @RequestHeader(value = USER_HEADER) @Positive Long userId) {
        return itemClient.get(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader(value = USER_HEADER) Long userId) {
        return itemClient.getListByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String query) {

        return itemClient.search(query);
    }
}