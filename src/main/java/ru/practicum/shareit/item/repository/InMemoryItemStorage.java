package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final List<Item> items = new ArrayList<>();
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public Item getItem(int id) {
        return items
                .stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item with id " + id + " not found"));
    }

    @Override
    public List<Item> getAllItemsByUserId(int userId) {
        return items
                .stream()
                .filter(i -> i.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item, int userId) {
        item.setId(generateId());
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item, int itemId, int userId) {
        Item findedItem = getItem(itemId);
        findedItem.setAvailable(item.getAvailable());
        findedItem.setDescription(item.getDescription());
        findedItem.setName(item.getName());
        return findedItem;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return items
                .stream()
                .filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase().trim()) ||
                        i.getDescription().toLowerCase().contains(text.toLowerCase().trim()))
                        && i.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public Comment addComment(Comment comment) {
        Item item = items.stream().filter(it -> it.getId() == comment.getItem().getId()).findFirst().orElse(null);
        if (item == null) {
            throw new NotFoundException("Item with id " + comment.getItem().getId() + " not found");
        }
        item.getComments().add(comment);
        return comment;
    }

    private int generateId() {
        return idGenerator.incrementAndGet();
    }
}
