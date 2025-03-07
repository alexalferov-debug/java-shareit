package ru.practicum.shareit.booking.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateBeforeEndDate  {
    String message() default "Дата окончания бронирования должна быть позже, даты его начала";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
