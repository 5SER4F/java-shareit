package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Long ownerId, Item item);

    Item patchItem(Long ownerId, Long itemId, Item item);

    Item getItemById(Long itemId);

    List<Item> getAllUserItem(Long ownerId);

    List<Item> findByTextQuery(String query);
}
