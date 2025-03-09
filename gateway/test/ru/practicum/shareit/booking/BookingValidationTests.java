package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingValidationTests {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    @MockBean
    private BookingClient bookingClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;


    private LocalDateTime validStart;
    private LocalDateTime validEnd;

    @BeforeEach
    void setUp() {
        validStart = LocalDateTime.now().plusHours(1);
        validEnd = LocalDateTime.now().plusHours(2);
    }

    @Test
    void createBookingWithValidDataShouldReturnOk() throws Exception {
        CreateBookingDto validDto = new CreateBookingDto(
                validStart,
                validEnd,
                1L
        );

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, 1)
                        .content(mapper.writeValueAsString(validDto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCreateBookingDto")
    void createBookingWithInvalidDataShouldReturnBadRequest(
            String fieldName,
            String errorMessage,
            CreateBookingDto invalidDto
    ) throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"))
                .andExpect(jsonPath("$." + fieldName).value(errorMessage));
    }

    private static Stream<Arguments> provideInvalidCreateBookingDto() {
        LocalDateTime pastDate = LocalDateTime.now().minusHours(1);
        LocalDateTime futureDate = LocalDateTime.now().plusHours(1);

        return Stream.of(
                Arguments.of(
                        "start",
                        "must not be null",
                        new CreateBookingDto(null, futureDate, 1L)
                ),

                Arguments.of(
                        "start",
                        "must be a date in the present or in the future",
                        new CreateBookingDto(pastDate, futureDate, 1L)
                ),

                Arguments.of(
                        "end",
                        "must not be null",
                        new CreateBookingDto(futureDate, null, 1L)
                ),

                Arguments.of(
                        "end",
                        "must be a future date",
                        new CreateBookingDto(futureDate, pastDate, 1L)
                ),

                Arguments.of(
                        "itemId",
                        "must be greater than 0",
                        new CreateBookingDto(futureDate, futureDate.plusHours(1), 0L)
                ),

                Arguments.of(
                        "itemId",
                        "must be greater than 0",
                        new CreateBookingDto(futureDate, futureDate.plusHours(1), -1L)
                ),

                Arguments.of(
                        "global",
                        "Дата окончания бронирования должна быть позже, даты его начала",
                        new CreateBookingDto(futureDate.plusHours(2), futureDate, 1L)
                )
        );
    }

}
