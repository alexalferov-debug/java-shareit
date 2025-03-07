package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto bookingDto, int userId);

    BookingDto updateBookingStatus(int bookingId, boolean status, int userId);

    BookingDto getBooking(int bookingId, int userId);

    List<BookingDto> getBookings(int userId, BookingState state, boolean isForOwner);

    List<Booking> getFinishedBookings(List<Integer> items);

    List<Booking> getNextBookings(List<Integer> items);
}
