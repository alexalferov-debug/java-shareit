package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.DataValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTests {
    @Mock
    private BookingStorage storage;
    @Mock
    private ItemStorage itemService;
    @Mock
    private UserStorage userService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Item item;
    private CreateBookingDto createBookingDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(new User(2L, "owner", "owner@mail.com"));

        createBookingDto = new CreateBookingDto();
        createBookingDto.setItemId(1L);
    }

    @Test
    void createBookingWhenUserNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> bookingService.createBooking(createBookingDto, 1L));
    }

    @Test
    void createBookingWhenItemNotAvailableThrowsException() {
        item.setAvailable(false);
        when(userService.getUser(1L)).thenReturn(user);
        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        assertThrows(DataValidationException.class, () -> bookingService.createBooking(createBookingDto, 1L));
    }

    @Test
    void createBookingWhenBookOwnItemThrowsException() {
        item.setOwner(user);
        when(userService.getUser(1L)).thenReturn(user);
        when(itemService.getItem(1L)).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(createBookingDto, 1L));
    }

    @Test
    void updateBookingStatusWhenNotOwnerThrowsException() {
        Booking booking = new Booking();
        booking.setItem(item);
        when(storage.getBooking(1)).thenReturn(booking);

        assertThrows(DataValidationException.class, () -> bookingService.updateBookingStatus(1, true, 999L));
    }

    @Test
    void getBookingWhenUnauthorizedUserThrowsException() {
        Booking booking = new Booking();
        booking.setBooker(new User(3L, "other", "other@mail.com"));
        booking.setItem(item);
        when(storage.getBooking(1)).thenReturn(booking);

        assertThrows(DataValidationException.class, () -> bookingService.getBooking(1, 999L));
    }

    @Test
    void getBookingsWhenNoBookingsThrowsException() {
        when(storage.getUserBookings(1L, BookingState.ALL, false)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> bookingService.getBookings(1L, BookingState.ALL, false));
    }
}