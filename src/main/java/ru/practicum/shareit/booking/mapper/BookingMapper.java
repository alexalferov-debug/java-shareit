package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "item", target = "item")
    @Mapping(source = "booker", target = "booker")
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);

    Booking toBooking(CreateBookingDto createBookingDto);
}
