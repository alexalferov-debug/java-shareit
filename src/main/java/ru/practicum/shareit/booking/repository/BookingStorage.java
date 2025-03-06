package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingStorage {
    Booking createBooking(Booking booking);

    Booking updateBookingStatus(Booking booking);

    Booking getBooking(int bookingId);

    List<Booking> getUserBookings(int userId, BookingState bookingState, boolean isForOwner);

    List<Booking> getFinishedBookings(List<Integer> items);

    List<Booking> getNextBookings(List<Integer> items);
}
