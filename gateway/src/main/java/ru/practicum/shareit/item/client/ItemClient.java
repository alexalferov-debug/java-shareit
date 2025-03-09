package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.AddCommentDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    @CacheEvict(value = {"itemCache", "itemsCache"}, allEntries = true)
    public ResponseEntity<Object> create(ItemRequestAddDto item, Long userId) {
        return post("", userId, item);
    }

    @CacheEvict(value = "itemCache", key = "#itemId")
    public ResponseEntity<Object> addComment(AddCommentDto comment, Long userId, Long itemId) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    @CacheEvict(value = {"itemCache", "itemsCache"}, key = "#itemId")
    public ResponseEntity<Object> update(ItemRequestPatchDto item, Long itemId, Long userId) {
        return patch("/" + itemId, userId, item);
    }

    @Cacheable(value = "itemCache", key = "{#itemId, #userId}")
    public ResponseEntity<Object> get(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    @Cacheable(value = "itemsCache", key = "#userId")
    public ResponseEntity<Object> getListByUserId(Long userId) {
        return get("", userId);
    }

    @Cacheable(value = "itemsCache", key = "#query")
    public ResponseEntity<Object> search(String query) {
        String path = UriComponentsBuilder.fromPath("/search")
                .queryParam("text", query)
                .toUriString();
        return get(path);
    }
}