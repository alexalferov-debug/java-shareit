package ru.practicum.shareit.request;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.RequestClient;
import ru.practicum.shareit.request.dto.RequestAddDto;

@RestController
@RequestMapping("/requests")
@Validated
public class RequestController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    RequestClient requestClient;

    @Autowired
    public RequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Validated RequestAddDto itemRequestDto, @RequestHeader(USER_HEADER) @Positive Long userId) {
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getAllItemRequestsFromAnotherUsers(@RequestHeader(USER_HEADER) Long userId) {
        return requestClient.getAllItemRequestsFromAnotherUsers(userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllRequestsFromCurrentUser(@RequestHeader(USER_HEADER) Long userId) {
        return requestClient.getAllRequestsFromCurrentUser(userId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("id") Long requestId) {
        return requestClient.getItemRequestById(requestId);
    }
}
