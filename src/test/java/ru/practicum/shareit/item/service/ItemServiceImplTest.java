package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.comment.CommentRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void addItemTest() {
        Long ownerId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Book");
        itemDto.setDescription("A book");

        Item item = new Item();
        item.setOwnerId(ownerId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());

        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(userRepository.existsById(any(Long.class))).thenReturn(true);

        Item result = itemService.addItem(ownerId, itemDto);

        assertNotNull(result);
        assertEquals(ownerId, result.getOwnerId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void patchItemTest() {
        Long ownerId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Book");
        itemDto.setDescription("A book");

        Item oldItem = new Item();
        oldItem.setId(itemId);
        oldItem.setOwnerId(ownerId);
        oldItem.setName("Old Book");
        oldItem.setDescription("An old book");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(oldItem));
        when(itemRepository.save(any(Item.class))).thenReturn(oldItem);

        when(commentRepository.findByItemId(any(Long.class))).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemOwner(any(Long.class))).thenReturn(Collections.emptyList());

        Item result = itemService.patchItem(ownerId, itemId, itemDto);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(ownerId, result.getOwnerId());
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemByIdTest() {
        Long itemId = 1L;
        Long requesterId = 2L;

        Item item = new Item();
        item.setId(itemId);
        item.setOwnerId(requesterId);
        item.setName("Book");
        item.setDescription("A book");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(any(Long.class))).thenReturn(Collections.emptyList());
        when(bookingRepository.findByItemOwner(any(Long.class))).thenReturn(Collections.emptyList());

        Item result = itemService.getItemById(itemId, requesterId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals(requesterId, result.getOwnerId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());

        verify(itemRepository, times(1)).findById(itemId);
    }
}