package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.ItemWithoutOwnerDto;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemControllerIntegrationTest {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    @Autowired
    private TestRestTemplate restTemplate;
    private UserDTO user;
    private ItemWithoutOwnerDto item;

    @BeforeEach
    void setUp() {
        UserDTO userRequest = new UserDTO(null, "user", "user@mail.com");
        user = restTemplate.postForObject("/users", userRequest, UserDTO.class);

        ItemWithoutOwnerDto itemRequest = new ItemWithoutOwnerDto();
        itemRequest.setName("Item");
        itemRequest.setDescription("Description");
        itemRequest.setAvailable(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_HEADER, user.getId().toString());
        item = restTemplate.exchange(
                "/items",
                HttpMethod.POST,
                new HttpEntity<>(itemRequest, headers),
                ItemWithoutOwnerDto.class
        ).getBody();
    }

    @Test
    void getAllByUserIdReturnsItemsWithBookings() {
        ResponseEntity<ItemWithoutOwnerDto[]> response = restTemplate.exchange(
                "/items",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(user.getId())),
                ItemWithoutOwnerDto[].class
        );
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().length);
        assertEquals("Item", response.getBody()[0].getName());
    }

    private HttpHeaders getHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(USER_HEADER, userId.toString());
        return headers;
    }
}
