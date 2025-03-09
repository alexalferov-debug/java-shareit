package ru.practicum.shareit.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserRequestAddDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
public class UserCacheTests {


    @Autowired
    private UserClient userClient;

    @Autowired
    private CacheManager cacheManager;

    private MockRestServiceServer mockServer;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(userClient.getRestTemplate());
    }

    @Test
    void testCache() throws Exception {
        UserDTO user = new UserDTO(1L, "John", "john@example.com");
        String userJson = mapper.writeValueAsString(user);
        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andRespond(withSuccess(userJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response1 = userClient.getUser(1L);
        UserDTO result1 = mapper.convertValue(response1.getBody(), UserDTO.class);
        assertEquals("John", result1.getName());
        Cache userCache = cacheManager.getCache("userCache");
        assertNotNull(userCache);
        ResponseEntity<?> cachedResponse = userCache.get(1L, ResponseEntity.class);
        assertNotNull(cachedResponse);
        assertEquals("John", mapper.convertValue(cachedResponse.getBody(), UserDTO.class).getName());
        ResponseEntity<Object> response2 = userClient.getUser(1L);
        UserDTO result2 = mapper.convertValue(response2.getBody(), UserDTO.class);
        assertEquals("John", result2.getName());
        mockServer.verify();
    }

    @Test
    void testCreateUserEvictsAllCacheEntries() throws Exception {
        UserRequestAddDto newUser = new UserRequestAddDto("Alice", "alice@example.com");
        UserDTO createdUser = new UserDTO(2L, "Alice", "alice@example.com");
        String responseJson = mapper.writeValueAsString(createdUser);
        Cache userCache = cacheManager.getCache("userCache");
        Cache usersCache = cacheManager.getCache("usersCache");
        userCache.put(1L, new UserDTO(1L, "John", "john@example.com"));
        usersCache.put("allUsers", List.of(
                new UserDTO(1L, "John", "john@example.com")
        ));
        mockServer.expect(requestTo("http://localhost:9090/users"))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = userClient.createUser(newUser);
        assertTrue(userCache.get(1L) == null, "userCache не очищен");
        assertTrue(usersCache.get("allUsers") == null, "usersCache не очищен");
        UserDTO result = mapper.convertValue(response.getBody(), UserDTO.class);
        assertEquals("Alice", result.getName());
        mockServer.verify();
    }
}
