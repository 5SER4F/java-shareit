package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;
import java.util.Map;

public interface ItemService {
    Item addItem(Long ownerId, ItemDto itemDto);

    Item patchItem(Long ownerId, Long itemId, ItemDto itemDto);

    Item getItemById(Long itemId, Long requesterId);

    List<Item> getAllUserItem(Long ownerId);

    List<Item> findByTextQuery(String query);

    Comment addComment(Long requesterId, Long itemId, Map<String, String> text);
}
