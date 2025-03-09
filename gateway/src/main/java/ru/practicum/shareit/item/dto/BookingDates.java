package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDates {
    private LocalDateTime start;
    private LocalDateTime end;
}
