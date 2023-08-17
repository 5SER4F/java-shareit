package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static ru.practicum.shareit.item.dto.ItemMapper.dtoToItem;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public Item addItem(Long ownerId, ItemDto itemDto) {
        userValidation(ownerId);
        return itemStorage.addItem(ownerId, dtoToItem(itemDto, ownerId));
    }

    @Override
    public Item patchItem(Long ownerId, Long itemId, ItemDto itemDto) {
        userValidation(ownerId);
        return itemStorage.patchItem(ownerId, itemId, dtoToItem(itemDto, ownerId));
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<Item> getAllUserItem(Long ownerId) {
        userValidation(ownerId);
        return itemStorage.getAllUserItem(ownerId);
    }

    @Override
    public List<Item> findByTextQuery(String query) {
        return itemStorage.findByTextQuery(query);
    }

    private void userValidation(long userId) {
        if (!userStorage.contain(userId)) {
            throw new ResourceNotFoundException(
                    "Запрос от несуществующего пользователя userid=" + userId
            );
        }
    }
}
