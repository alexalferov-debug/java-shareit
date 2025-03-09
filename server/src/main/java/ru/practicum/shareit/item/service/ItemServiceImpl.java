package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.exception.DataValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.BookingDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestsStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final UserStorage userService;
    private final BookingStorage bookingService;
    private final ItemStorage itemStorage;
    private final ItemRequestsStorage itemRequestsStorage;

    @Autowired
    public ItemServiceImpl(UserStorage userService,
                           ItemStorage itemStorage,
                           BookingStorage bookingService,
                           ItemRequestsStorage itemRequestsStorage) {
        this.userService = userService;
        this.itemStorage = itemStorage;
        this.bookingService = bookingService;
        this.itemRequestsStorage = itemRequestsStorage;
    }

    public ItemDto getItem(Long id, Long userId) {
        Item item = itemStorage.getItem(id)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + id));
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> nextBookings = bookingService.getNextBookings(List.of(item.getId()));
            List<Booking> finishedBookings = bookingService.getFinishedBookings(List.of(item.getId()));
            if (!finishedBookings.isEmpty()) {
                item.setLastBooking(new BookingDates());
                item.getLastBooking().setStart(finishedBookings.getFirst().getStart());
                item.getLastBooking().setEnd(finishedBookings.getFirst().getEnd());
            }
            if (!nextBookings.isEmpty()) {
                item.setNextBooking(new BookingDates());
                item.getNextBooking().setStart(nextBookings.getFirst().getStart());
                item.getNextBooking().setEnd(nextBookings.getFirst().getEnd());
            }
        }
        return ItemMapper.INSTANCE.toItemDto(item);
    }


    public ItemWithoutOwnerDto createItem(ItemRequestAddDto itemAdd, Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        Item item = ItemMapper.INSTANCE.toItem(itemAdd);
        item.setOwner(user);
        if (itemAdd.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestsStorage.getItemRequest(itemAdd.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Request not found with id=" + itemAdd.getRequestId()));
            item.setRequest(itemRequest);
        }
        return ItemMapper.INSTANCE.toItemWithoutOwnerDto(itemStorage.addItem(item, userId));
    }

    public List<ItemWithoutOwnerDto> getAllItems(Long userId) {
        userService.getUser(userId);
        List<Item> items = itemStorage.getAllItemsByUserId(userId);
        List<Booking> nextBookings = bookingService.getNextBookings(items
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        List<Booking> finishedBookings = bookingService.getFinishedBookings(items
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return items
                .stream()
                .map(item -> {
                    Booking lastFinishedBooking = finishedBookings.stream().filter(it -> it.getItem().getId() == item.getId()).findFirst().orElse(null);
                    Booking nextBooking = nextBookings.stream().filter(it -> it.getItem().getId() == item.getId()).findFirst().orElse(null);
                    if (lastFinishedBooking != null) {
                        item.setLastBooking(new BookingDates());
                        item.getLastBooking().setStart(lastFinishedBooking.getStart());
                        item.getLastBooking().setEnd(lastFinishedBooking.getEnd());
                    }
                    if (nextBooking != null) {
                        item.setNextBooking(new BookingDates());
                        item.getNextBooking().setStart(nextBooking.getStart());
                        item.getNextBooking().setEnd(nextBooking.getEnd());
                    }
                    return ItemMapper.INSTANCE.toItemWithoutOwnerDto(item);
                })
                .collect(Collectors.toList());
    }

    public ItemWithoutOwnerDto updateItem(ItemRequestPatchDto item, Long itemId, Long userId) {
        Item findedItem = itemStorage.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + itemId));
        if (!findedItem.getOwner().getId().equals(userId))
            throw new NotFoundException("Запрещена модификация чужих вещей");
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (item.getName() != null) {
            findedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            findedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            findedItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.INSTANCE.toItemWithoutOwnerDto(itemStorage.updateItem(findedItem.setOwner(user), itemId, userId));
    }

    public List<ItemWithoutOwnerDto> searchItem(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.searchItem(text).stream().map(ItemMapper.INSTANCE::toItemWithoutOwnerDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(AddCommentDto comment, Long itemId, Long userId) {
        Item item = itemStorage.getItem(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + itemId));
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        List<Booking> bookings = bookingService.getFinishedBookings(List.of(item.getId()));
        if (bookings.stream().noneMatch(it -> it.getBooker().getId().equals(user.getId()))) {
            throw new DataValidationException("Отзыв можно оставить только для вещи, которую вы брали в аренду");
        }
        Comment createdComment = new Comment();
        createdComment.setText(comment.getText());
        createdComment.setCreated(LocalDateTime.now());
        createdComment.setAuthor(user);
        createdComment.setItem(item);
        return CommentMapper.INSTANCE.toCommentDto(itemStorage.addComment(createdComment));
    }
}
