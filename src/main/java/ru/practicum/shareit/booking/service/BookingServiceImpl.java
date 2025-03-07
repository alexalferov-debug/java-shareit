package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.exception.DataValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage storage;
    private final ItemStorage itemService;
    private final UserStorage userService;

    @Autowired
    public BookingServiceImpl(BookingStorage storage,
                              ItemStorage itemService,
                              UserStorage userService) {
        this.storage = storage;
        this.itemService = itemService;
        this.userService = userService;
    }

    public BookingDto createBooking(CreateBookingDto bookingDto, int userId) {
        Booking booking = BookingMapper.INSTANCE.toBooking(bookingDto);
        Item item = itemService.getItem(bookingDto.getItemId());
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        User userDTO = userService.getUser(userId);
        if (userDTO == null) {
            throw new NotFoundException("User not found");
        }
        if (!item.getAvailable()) {
            throw new DataValidationException("Item is not available");
        }
        if (item.getOwner().getId() == userDTO.getId()) {
            throw new NotFoundException("Невозможно забронировать свою вещь");
        }
        booking.setItem(item);
        booking.setBooker(userDTO);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.INSTANCE.toBookingDto(storage.createBooking(booking));
    }

    public BookingDto updateBookingStatus(int bookingId, boolean status, int userId) {
        Booking booking = storage.getBooking(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }
        if (booking.getItem().getOwner().getId() != userId) {
            throw new DataValidationException("Операция доступна только владельцу вещи");
        }
        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.INSTANCE.toBookingDto(storage.updateBookingStatus(booking));
    }

    public BookingDto getBooking(int bookingId, int userId) {
        Booking booking = storage.getBooking(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking not found");
        }
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new DataValidationException("Операция доступна только владельцу вещи или автору бронирования");
        }
        return BookingMapper.INSTANCE.toBookingDto(storage.getBooking(bookingId));
    }

    public List<BookingDto> getBookings(int userId, BookingState state, boolean isForOwner) {
        List<Booking> bookings;
        bookings = storage.getUserBookings(userId, state, isForOwner);
        if (bookings.isEmpty()) {
            throw new NotFoundException("Booking not found");
        }
        return bookings.stream().map(BookingMapper.INSTANCE::toBookingDto).collect(Collectors.toList());
    }

    public List<Booking> getFinishedBookings(List<Integer> items) {
        return storage.getFinishedBookings(items);
    }

    public List<Booking> getNextBookings(List<Integer> items) {
        return storage.getNextBookings(items);
    }
}
