package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.validator.StartDateBeforeEndDate;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@StartDateBeforeEndDate
public class CreateBookingDto {
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;
    @Future
    @NotNull
    private LocalDateTime end;
    @NotNull
    @Positive
    private int itemId;
}
