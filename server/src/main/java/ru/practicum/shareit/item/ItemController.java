package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.itemToDto;
import static ru.practicum.shareit.item.dto.ItemMapper.itemToDtoWithBooking;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                           @RequestBody @Valid ItemDto itemDto) {
        return new ResponseEntity<>(itemToDto(service.addItem(ownerId, itemDto)), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                             @PathVariable("itemId") Long itemId,
                                             @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemToDto(service.patchItem(ownerId, itemId, itemDto)), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                               @PathVariable("itemId") Long itemId) {
        return new ResponseEntity<>(itemToDtoWithBooking(service.getItemById(itemId, requesterId)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllUserItem(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        List<ItemDto> userItems = service.getAllUserItem(ownerId).stream()
                .sorted(Comparator.comparingLong(Item::getId))
                .map(ItemMapper::itemToDtoWithBooking)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> findByTextQuery(@RequestParam("text") String query) {
        List<ItemDto> items = service.findByTextQuery(query).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                                 @PathVariable("itemId") Long itemId,
                                                 @RequestBody Map<String, String> text) {
        return new ResponseEntity<>(CommentMapper.modelToDto(service.addComment(requesterId, itemId, text)),
                HttpStatus.OK);
    }
}
