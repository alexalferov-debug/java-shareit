package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    UserService userService;
    ItemStorage itemStorage;

    @Autowired
    ItemServiceImpl(UserService userService, ItemStorage itemStorage) {
        this.userService = userService;
        this.itemStorage = itemStorage;
    }

    public ItemDto getItem(int id) {
        return new ItemDto(itemStorage.getItem(id));
    }


    public ItemDto createItem(ItemRequestAddDto item, int userId) {
        userService.getUser(userId);
        return new ItemDto(itemStorage.addItem(item.toItem(), userId));
    }

    public List<ItemDto> getAllItems(int userId) {
        userService.getUser(userId);
        return itemStorage.getAllItemsByUserId(userId).stream().map(ItemDto::new).collect(Collectors.toList());
    }

    public ItemDto updateItem(ItemRequestPatchDto item, int itemId, int userId) {
        userService.getUser(userId);
        return new ItemDto(itemStorage.updateItem(item.toItem(), itemId, userId));
    }

    public List<ItemDto> searchItem(String text) {
        return itemStorage.searchItem(text).stream().map(ItemDto::new).collect(Collectors.toList());
    }
}
