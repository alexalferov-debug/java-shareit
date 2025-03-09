package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@AllArgsConstructor
public class ItemWithoutRequestsDto {
    private Long id;
    private String name;
    private String description;
    private UserDTO owner;
    private Boolean available;
    private BookingDates lastBooking;
    private BookingDates nextBooking;
    private List<CommentDto> comments;
}
