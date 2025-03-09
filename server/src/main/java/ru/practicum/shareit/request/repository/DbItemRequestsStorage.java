package ru.practicum.shareit.request.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Transactional(readOnly = true)
public class DbItemRequestsStorage implements ItemRequestsStorage {
    private final ItemRequestRepository itemRequestRepository;

    @Autowired
    public DbItemRequestsStorage(ItemRequestRepository itemRequestRepository) {
        this.itemRequestRepository = itemRequestRepository;
    }

    @Transactional
    public ItemRequest addItemRequest(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }

    public Optional<ItemRequest> getItemRequest(Long id) {
        return itemRequestRepository.findById(id);
    }

    public List<ItemRequest> getItemRequestsFromAnotherUsers(Long userId) {
        return itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId);
    }

    public List<ItemRequest> getItemRequestsByUserId(Long userId) {
        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId);
    }
}
