package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private final List<Item> items = new ArrayList<>();
    private static final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Optional<Item> getItem(Long id) {
        return items
                .stream()
                .filter(i -> i.getId() == id)
                .findFirst();
    }

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return items
                .stream()
                .filter(i -> i.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item, Long userId) {
        item.setId(generateId());
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        Item findedItem = getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + itemId));
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

    private Long generateId() {
        return idGenerator.incrementAndGet();
    }
}
