package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;


    @Test
    void addPostShouldThrowResourceNotFoundExceptionWhenRequesterNotFound() {
        Map<String, String> requestContent = new HashMap<>();
        requestContent.put("description", "Test description");


        assertThrows(ResourceNotFoundException.class, () -> {
            itemRequestService.addPost(1L, requestContent);
        });

        verify(itemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void addPostWouldThrowFailedItemRequestExceptionWhenDescriptionMissing() {
        Map<String, String> requestContent = new HashMap<>();

        assertThrows(ResourceNotFoundException.class, () -> {
            itemRequestService.addPost(1L, requestContent);
        });

        verify(itemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void addPostShouldSaveItemRequestWhenRequestIsValid() {
        Map<String, String> requestContent = new HashMap<>();
        requestContent.put("description", "Test description");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemRequest result = itemRequestService.addPost(1L, requestContent);

        assertNotNull(result);
        assertEquals(1L, result.getRequesterId());
        assertEquals("Test description", result.getDescription());
        assertNotNull(result.getCreated());

        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getByIdShouldThrowResourceNotFoundExceptionWhenRequesterNotFound() {

        assertThrows(ResourceNotFoundException.class, () -> {
            itemRequestService.getById(1L, 1L);
        });

        verify(itemRequestRepository, never()).findById(anyLong());
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenRequestNotFound() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            itemRequestService.getById(1L, 1L);
        });

        verify(itemRequestRepository, times(1)).findById(anyLong());
        verify(itemRepository, never()).findAllByRequestIdIn(anyList());
    }
}