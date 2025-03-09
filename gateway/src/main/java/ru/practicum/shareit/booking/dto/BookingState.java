package ru.practicum.shareit.booking.dto;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState getByName(String name) {
        return Arrays.stream(BookingState.values())
                .filter(x -> x.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
