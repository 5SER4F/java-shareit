package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                          @RequestBody @Valid ItemDto itemDto) {
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                            @PathVariable("itemId") Long itemId,
                                            @RequestBody ItemDto itemDto) {
        return itemClient.patchItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                              @PathVariable("itemId") Long itemId) {
        return itemClient.getItemById(requesterId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItem(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.getAllUserItem(ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findByTextQuery(@RequestParam("text") String query) {
        return itemClient.findByTextQuery(query);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody Map<String, String> text) {
        return itemClient.addComment(requesterId, itemId, text);
    }
}
