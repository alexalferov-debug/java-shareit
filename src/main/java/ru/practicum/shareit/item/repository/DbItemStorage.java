package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.model.comment.CommentRepository;

import java.util.List;

@Repository
@Primary
@Transactional(readOnly = true)
public class DbItemStorage implements ItemStorage {
    ItemRepository itemRepository;
    CommentRepository commentRepository;

    @Autowired
    public DbItemStorage(ItemRepository repository,
                         CommentRepository commentRepository) {
        this.itemRepository = repository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Item getItem(int id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> getAllItemsByUserId(int userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    @Transactional
    public Item addItem(Item item, int userId) {
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Item item, int itemId, int userId) {
        return itemRepository.save(item);
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemRepository.searchItems(text);
    }

    @Override
    @Transactional
    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
