package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.mapper.RequestsMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestsStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestsService {
    ItemStorage itemStorage;
    UserStorage userStorage;
    ItemRequestsStorage itemRequestsStorage;

    @Autowired
    public ItemRequestsService(ItemStorage itemStorage,
                               UserStorage userStorage,
                               ItemRequestsStorage itemRequestsStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemRequestsStorage = itemRequestsStorage;
    }

    public ItemRequestDto addItemRequest(RequestAddDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = RequestsMapper.INSTANCE.toEntity(itemRequestDto);
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id " + userId);
        }
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        return RequestsMapper.INSTANCE.toDto(itemRequestsStorage.addItemRequest(itemRequest));
    }

    public ItemRequestDto getItemRequest(Long requestId) {
        return RequestsMapper.INSTANCE.toDto(itemRequestsStorage.getItemRequest(requestId).orElseThrow(() -> new NotFoundException("Request not found with id " + requestId)));
    }

    public List<ItemRequestDto> getItemRequestsFromAnotherUsers(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id " + userId);
        }
        return itemRequestsStorage.getItemRequestsFromAnotherUsers(userId).stream().map(RequestsMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    public List<ItemRequestDto> getItemRequestsForCurrentUser(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found with id " + userId);
        }
        return itemRequestsStorage.getItemRequestsByUserId(userId).stream().map(RequestsMapper.INSTANCE::toDto).collect(Collectors.toList());
    }
}
