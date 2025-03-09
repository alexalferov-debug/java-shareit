package ru.practicum.shareit.item.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDates {
    private LocalDateTime start;
    private LocalDateTime end;
}
