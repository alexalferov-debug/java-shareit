package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto bookingDto, Long userId);

    BookingDto updateBookingStatus(int bookingId, boolean status, Long userId);

    BookingDto getBooking(int bookingId, Long userId);

    List<BookingDto> getBookings(Long userId, BookingState state, boolean isForOwner);

    List<Booking> getFinishedBookings(List<Long> items);

    List<Booking> getNextBookings(List<Long> items);
}
