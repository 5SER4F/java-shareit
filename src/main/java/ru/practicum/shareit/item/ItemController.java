package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.itemToDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    ResponseEntity<ItemDto> addItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                    @RequestBody @Valid ItemDto itemDto) {
        return new ResponseEntity<>(itemToDto(service.addItem(ownerId, itemDto)), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<ItemDto> patchItem(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                      @PathVariable("itemId") Long itemId,
                                      @RequestBody ItemDto itemDto) {
        return new ResponseEntity<>(itemToDto(service.patchItem(ownerId, itemId, itemDto)), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<ItemDto> getItemById(@PathVariable("itemId") Long itemId) {
        return new ResponseEntity<>(itemToDto(service.getItemById(itemId)), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<ItemDto>> getAllUserItem(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        List<ItemDto> userItems = service.getAllUserItem(ownerId).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userItems, HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<List<ItemDto>> findByTextQuery(@RequestParam("text") String query) {
        List<ItemDto> items = service.findByTextQuery(query).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
