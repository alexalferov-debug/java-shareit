package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final static String USER_HEADER = "X-Sharer-User-Id";

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Valid CreateBookingDto bookingDto, @RequestHeader(value = USER_HEADER) @Positive int userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(bookingDto, userId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> updateBookingStatus(@PathVariable("id") @Positive int id, @RequestParam(name = "approved") boolean approved, @RequestHeader(value = USER_HEADER) @Positive int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBookingStatus(id, approved, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable("id") @Positive int id, @RequestHeader(value = USER_HEADER) @Positive int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBooking(id, userId));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingsForOwner(@RequestParam(name = "state", required = false) BookingState state, @RequestHeader(value = USER_HEADER) @Positive int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookings(userId, state, true));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings(@RequestParam(name = "state", required = false) BookingState state, @RequestHeader(value = USER_HEADER) @Positive int userId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookings(userId, state, false));
    }
}
