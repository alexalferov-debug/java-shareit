package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.service.ItemRequestsService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    ItemRequestsService itemRequestsService;

    @Autowired
    public ItemRequestController(ItemRequestsService itemRequestsService) {
        this.itemRequestsService = itemRequestsService;
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> addItem(@RequestBody RequestAddDto itemRequestDto, @RequestHeader(USER_HEADER) Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemRequestsService.addItemRequest(itemRequestDto, userId));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ItemRequestDto>> getAllItemRequestsFromAnotherUsers(@RequestHeader(USER_HEADER) Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestsService.getItemRequestsFromAnotherUsers(userId));
    }

    @GetMapping()
    public ResponseEntity<List<ItemRequestDto>> getAllRequestsFromCurrentUser(@RequestHeader(USER_HEADER) Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestsService.getItemRequestsForCurrentUser(userId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemRequestDto> getItemRequestById(@PathVariable("id") Long requestId) {
        return ResponseEntity.status(HttpStatus.OK).body(itemRequestsService.getItemRequest(requestId));
    }
}
