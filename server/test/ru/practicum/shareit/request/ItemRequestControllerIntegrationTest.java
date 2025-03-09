package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.user.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ItemRequestControllerIntegrationTest {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    @Autowired
    private TestRestTemplate restTemplate;
    private UserDTO user,cheker;

    @BeforeEach
    void setUp() {
        UserDTO userRequest = new UserDTO(null, "user", "user@mail.com");
        UserDTO chekerRequest = new UserDTO(null, "checker", "checker@mail.com");
        user = restTemplate.postForObject("/users", userRequest, UserDTO.class);
        cheker = restTemplate.postForObject("/users", userRequest, UserDTO.class);
    }
    @Test
    void getAllItemRequestsFromAnotherUsersReturnsRequests() {
        RequestAddDto request = new RequestAddDto();
        request.setDescription("Need item");
        restTemplate.exchange(
                "/requests",
                HttpMethod.POST,
                new HttpEntity<>(request, getHeaders(user.getId())),
                ItemRequestDto.class
        );
        ResponseEntity<ItemRequestDto[]> response = restTemplate.exchange(
                "/requests/all",
                HttpMethod.GET,
                new HttpEntity<>(getHeaders(cheker.getId())),
                ItemRequestDto[].class
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

