package ru.practicum.shareit.request.repository;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestsStorage {
    ItemRequest addItemRequest(ItemRequest itemRequest);

    Optional<ItemRequest> getItemRequest(Long id);

    List<ItemRequest> getItemRequestsFromAnotherUsers(Long userId);

    List<ItemRequest> getItemRequestsByUserId(Long userId);
}
