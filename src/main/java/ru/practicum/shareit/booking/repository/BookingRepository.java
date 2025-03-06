package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(long bookingId);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findBookingsByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime startTime, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByItemOwnerIdAndEndIsBeforeAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfterAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);


    List<Booking> findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime startTime, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByBookerIdAndEndIsBeforeAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByBookerIdAndStartIsAfterAndStatusIsNotOrderByStartDesc(long ownerId, LocalDateTime endTime,BookingStatus status);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.end < :now " +
            "ORDER BY b.end DESC")
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Integer> itemIds,
                                           @Param("now") LocalDateTime now);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id IN :itemIds " +
            "AND b.start > :now " +
            "ORDER BY b.start ASC")
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Integer> itemIds,
                                           @Param("now") LocalDateTime now);

}
