package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(Long ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> patchItem(Long ownerId, Long itemId, ItemDto itemDto) {
        return patch("/" + itemId, ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long requesterId, Long itemId) {
        return get("/" + itemId, requesterId);
    }

    public ResponseEntity<Object> getAllUserItem(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> findByTextQuery(String query) {
        return get("/search?text=" + query);
    }

    public ResponseEntity<Object> addComment(Long requesterId, Long itemId, Map<String, String> text) {
        return post("/" + itemId + "/comment", requesterId, text);
    }
}
