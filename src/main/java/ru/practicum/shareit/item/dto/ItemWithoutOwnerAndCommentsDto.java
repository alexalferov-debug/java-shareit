package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.model.BookingDates;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@AllArgsConstructor
public class ItemWithoutOwnerAndCommentsDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDates lastBooking;
    private BookingDates nextBooking;
}
