package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.Patcher;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, List<Item>> storage = new HashMap<>();
    private long idCounter = 0;

    @Override
    public Item addItem(Long ownerId, Item item) {
        item.setOwnerId(ownerId);
        item.setId(getNewId());
        storage.compute(
                ownerId,
                (id, items) -> {
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                    items.add(item);
                    return items;
                }
        );
        return item;
    }

    @Override
    public Item patchItem(Long ownerId, Long itemId, Item itemPatch) {
        storage.compute(
                ownerId,
                (Long id, List<Item> items) -> {
                    if (items == null || items.stream().noneMatch(i -> i.getId() == itemId)) {
                        throw new ResourceNotFoundException(String.format(
                                "Вещь id=%d, пользователя ownerId=%d не найдена", itemId, ownerId
                        ));
                    }
                    Item oldItem = items.stream().filter(i -> i.getId() == itemId).findFirst().get();
                    Patcher.setIfNotNull(oldItem::setAvailable, itemPatch.isAvailable());
                    Patcher.setIfNotNull(oldItem::setName, itemPatch.getName());
                    Patcher.setIfNotNull(oldItem::setDescription, itemPatch.getDescription());
                    return items;
                }
        );
        return storage.get(ownerId).stream()
                .filter(i -> i.getId() == itemId)
                .findAny()
                .get();
    }

    @Override
    public Item getItemById(Long itemId) {
        return storage.values().stream()
                .flatMap(List::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Вещь не найдена id=" + itemId);
                });
    }

    @Override
    public List<Item> getAllUserItem(Long ownerId) {
        return List.copyOf(storage.get(ownerId));
    }

    @Override
    public List<Item> findByTextQuery(String query) {
        if ("".equals(query)) {
            return Collections.emptyList();
        }
        String formattedQuery = query.toLowerCase();
        return storage.values().stream()
                .flatMap(List::stream)
                .filter(Item::isAvailable)
                .filter((Item item) -> item.getDescription().toLowerCase().contains(formattedQuery)
                        || item.getName().toLowerCase().contains(formattedQuery))
                .collect(Collectors.toList());
    }

    private long getNewId() {
        return ++idCounter;
    }
}
