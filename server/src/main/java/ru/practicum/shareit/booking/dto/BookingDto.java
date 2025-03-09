package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerAndCommentsDto;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemWithoutOwnerAndCommentsDto item;
    private UserDTO booker;
    private BookingStatus status;
}
