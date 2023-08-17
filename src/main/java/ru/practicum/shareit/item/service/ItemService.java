package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Long ownerId, ItemDto itemDto);

    Item patchItem(Long ownerId, Long itemId, ItemDto itemDto);

    Item getItemById(Long itemId);

    List<Item> getAllUserItem(Long ownerId);

    List<Item> findByTextQuery(String query);
}
