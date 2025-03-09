package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerIntegrationTests {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    @Autowired
    private TestRestTemplate restTemplate;
    private UserDTO user;
    private CreateBookingDto bookingDto;

    @BeforeEach
    void setUp() {
        UserDTO userRequest = new UserDTO(null, "user", "user@mail.com");
        UserDTO bookerRequest = new UserDTO(null, "booker", "booker@mail.com");
        user = restTemplate.postForObject("/users", userRequest, UserDTO.class);
        UserDTO booker = restTemplate.postForObject("/users", bookerRequest, UserDTO.class);
        ItemWithoutOwnerDto itemRequest = new ItemWithoutOwnerDto();
        itemRequest.setName("Item");
        itemRequest.setDescription("Description");
        itemRequest.setAvailable(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_HEADER, booker.getId().toString());
        ItemWithoutOwnerDto item = restTemplate.exchange(
                "/items",
                HttpMethod.POST,
                new HttpEntity<>(itemRequest, headers),
                ItemWithoutOwnerDto.class
        ).getBody();
        bookingDto = new CreateBookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    void getBookingsReturnsAllBookings() {
        restTemplate.exchange(
                "/bookings",
                HttpMethod.POST,
                new HttpEntity<>(bookingDto, getHeaders(user.getId())),
                BookingDto.class
        );
        ResponseEntity<BookingDto[]> response = restTemplate.exchange(
                "/bookings?state=ALL",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(user.getId())),
                BookingDto[].class
        );
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().length > 0);
    }
    private HttpHeaders getHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_HEADER, userId.toString());
        return headers;
    }
}

