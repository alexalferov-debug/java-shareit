package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserRequestAddDto;
import ru.practicum.shareit.user.dto.UserRequestPatchDto;

@Service
@EnableCaching
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public RestTemplate getRestTemplate() {
        return this.rest;
    }

    @CacheEvict(value = {"userCache", "usersCache"}, allEntries = true)
    public ResponseEntity<Object> createUser(UserRequestAddDto user) {
        return post("", user);
    }

    @CacheEvict(value = {"userCache", "usersCache"}, key = "#id")
    public ResponseEntity<Object> updateUser(Long id, UserRequestPatchDto user) {
        return patch("/" + id, user);
    }

    @CacheEvict(value = {"userCache", "usersCache"}, key = "#id")
    public ResponseEntity<Object> deleteUser(Long id) {
        return delete("/" + id);
    }

    @Cacheable(value = "userCache", key = "#id")
    public ResponseEntity<Object> getUser(Long id) {
        return get("/" + id);
    }

    @Cacheable(value = "usersCache", key = "'allUsers'")
    public ResponseEntity<Object> getUserList() {
        return get("");
    }
}