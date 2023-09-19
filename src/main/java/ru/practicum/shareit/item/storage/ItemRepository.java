package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemInRequestProjection;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String queryName, String queryDescription);

    List<ItemInRequestProjection> findAllByRequestIdIn(Collection<Long> requesterIds);

}
