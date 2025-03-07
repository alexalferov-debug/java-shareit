package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.BookingDates;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@AllArgsConstructor
public class ItemWithoutOwnerDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDates lastBooking;
    private BookingDates nextBooking;
    private List<CommentDto> comments;
}
