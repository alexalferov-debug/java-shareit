package ru.practicum.shareit.booking.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DbBookingStorage implements BookingStorage {
    BookingRepository repository;

    @Autowired
    public DbBookingStorage(BookingRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        return repository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Booking booking) {
        return repository.save(booking);
    }

    @Override
    public Booking getBooking(int bookingId) {
        return repository.findById(bookingId);
    }

    @Override
    public List<Booking> getUserBookings(int userId, BookingState bookingState, boolean isForOwner) {
        if (bookingState == null) {
            bookingState = BookingState.ALL;
        }
        switch (bookingState) {
            case REJECTED -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                } else {
                    return repository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                }
            }
            case WAITING -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                } else {
                    return repository.findBookingsByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                }
            }
            case PAST -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdAndEndIsBeforeAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), BookingStatus.REJECTED);
                } else {
                    return repository.findBookingsByBookerIdAndEndIsBeforeAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), BookingStatus.REJECTED);
                }
            }
            case FUTURE -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdAndStartIsAfterAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), BookingStatus.REJECTED);
                } else {
                    return repository.findBookingsByBookerIdAndStartIsAfterAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), BookingStatus.REJECTED);
                }
            }
            case CURRENT -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.REJECTED);
                } else {
                    return repository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterAndStatusIsNotOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), BookingStatus.REJECTED);
                }
            }
            default -> {
                if (isForOwner) {
                    return repository.findBookingsByItemOwnerIdOrderByStartDesc(userId);
                } else {
                    return repository.findBookingsByBookerIdOrderByStartDesc(userId);
                }
            }
        }
    }

    public List<Booking> getFinishedBookings(List<Integer> items) {
        return repository.findLastBookingsForItems(items, LocalDateTime.now());
    }

    public List<Booking> getNextBookings(List<Integer> items) {
        return repository.findNextBookingsForItems(items, LocalDateTime.now());
    }

}
