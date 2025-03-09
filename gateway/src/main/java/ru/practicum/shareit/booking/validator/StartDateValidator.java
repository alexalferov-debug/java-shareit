package ru.practicum.shareit.booking.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

public class StartDateValidator implements ConstraintValidator<StartDateBeforeEndDate, CreateBookingDto> {

    @Override
    public boolean isValid(CreateBookingDto bookingDto, ConstraintValidatorContext context) {
        if (bookingDto == null) {
            return false;
        }

        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();

        if (start == null || end == null) {
            return false;
        }

        return start.isBefore(end);
    }
}