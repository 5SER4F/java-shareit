package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.exception.exceptions.FailedItemRequestException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemInRequestProjection;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemRequestServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ItemRequestServiceImplTest {
    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestServiceImpl itemRequestServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testAddPostFailedItemRequestException() {
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(FailedItemRequestException.class, () -> itemRequestServiceImpl.addPost(1L, new HashMap<>()));
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testAddPostResourceNotFoundException() {
        when(userRepository.existsById((Long) any())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.addPost(1L, new HashMap<>()));
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testAddPostGood() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(mock(Timestamp.class));
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);
        when(itemRequestRepository.save((ItemRequest) any())).thenReturn(itemRequest);
        when(userRepository.existsById((Long) any())).thenReturn(true);

        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("description", "42");
        assertSame(itemRequest, itemRequestServiceImpl.addPost(1L, stringStringMap));
        verify(itemRequestRepository).save((ItemRequest) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testAddPostResourceNotFoundExceptionWithValid() {
        when(itemRequestRepository.save((ItemRequest) any()))
                .thenThrow(new ResourceNotFoundException("error"));
        when(userRepository.existsById((Long) any())).thenReturn(true);

        HashMap<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("description", "42");
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.addPost(1L, stringStringMap));
        verify(itemRequestRepository).save((ItemRequest) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testGetById() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(mock(Timestamp.class));
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        ArrayList<ItemInRequestProjection> itemInRequestProjectionList = new ArrayList<>();
        itemRequest.setItems(itemInRequestProjectionList);
        itemRequest.setRequesterId(1L);

        Optional<ItemRequest> ofResult = Optional.of(itemRequest);
        when(itemRequestRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        ItemRequest actualById = itemRequestServiceImpl.getById(1L, 1L);
        assertSame(itemRequest, actualById);
        assertEquals(itemInRequestProjectionList, actualById.getItems());
        verify(itemRequestRepository).findById((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetByIdResourceNotFoundException() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(mock(Timestamp.class));
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);

        Optional<ItemRequest> ofResult = Optional.of(itemRequest);
        when(itemRequestRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any()))
                .thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.getById(1L, 1L));
        verify(itemRequestRepository).findById((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetByIdGood() {
        ItemRequest itemRequest = mock(ItemRequest.class);
        doNothing().when(itemRequest).setCreated((Timestamp) any());
        doNothing().when(itemRequest).setDescription((String) any());
        doNothing().when(itemRequest).setId((Long) any());
        doNothing().when(itemRequest).setItems((List<ItemInRequestProjection>) any());
        doNothing().when(itemRequest).setRequesterId((Long) any());
        itemRequest.setCreated(mock(Timestamp.class));
        itemRequest.setDescription("something");
        itemRequest.setId(1L);

        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);
        Optional<ItemRequest> ofResult = Optional.of(itemRequest);
        when(itemRequestRepository.findById((Long) any())).thenReturn(ofResult);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        itemRequestServiceImpl.getById(1L, 1L);
        verify(itemRequestRepository).findById((Long) any());
        verify(itemRequest).setCreated((Timestamp) any());
        verify(itemRequest).setDescription((String) any());
        verify(itemRequest).setId((Long) any());
        verify(itemRequest, atLeast(1)).setItems((List<ItemInRequestProjection>) any());
        verify(itemRequest).setRequesterId((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllUserRequests() {
        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        when(itemRequestRepository.findByRequesterId((Long) any())).thenReturn(itemRequestList);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        List<ItemRequest> actualAllUserRequests = itemRequestServiceImpl.getAllUserRequests(1L);
        assertSame(itemRequestList, actualAllUserRequests);
        assertTrue(actualAllUserRequests.isEmpty());
        verify(itemRequestRepository).findByRequesterId((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllUserRequestsResourceNotFoundException() {
        when(itemRequestRepository.findByRequesterId((Long) any())).thenReturn(new ArrayList<>());
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any()))
                .thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.getAllUserRequests(1L));
        verify(itemRequestRepository).findByRequesterId((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllUserRequestsAnotherList() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(mock(Timestamp.class));
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(itemRequestRepository.findByRequesterId((Long) any())).thenReturn(itemRequestList);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        List<ItemRequest> actualAllUserRequests = itemRequestServiceImpl.getAllUserRequests(1L);
        assertSame(itemRequestList, actualAllUserRequests);
        assertEquals(1, actualAllUserRequests.size());
        verify(itemRequestRepository).findByRequesterId((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllUserRequestsResourceNotFoundExceptionValid() {
        when(itemRequestRepository.findByRequesterId((Long) any())).thenReturn(new ArrayList<>());
        when(userRepository.existsById((Long) any())).thenReturn(false);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.getAllUserRequests(1L));
        verify(userRepository).existsById((Long) any());
    }


    @Test
    void testGetAllPageable() {
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemRequestServiceImpl.getAllPageable(1L, 1, 3).isEmpty());
        verify(itemRequestRepository).findAll((Pageable) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllPageableResourceNotFoundException() {
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any()))
                .thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.getAllPageable(1L, 1, 3));
        verify(itemRequestRepository).findAll((Pageable) any());
        verify(userRepository).existsById((Long) any());
        verify(itemRepository).findAllByRequestIdIn((Collection<Long>) any());
    }

    @Test
    void testGetAllPageableResourceNotFoundExceptionValid() {
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(userRepository.existsById((Long) any())).thenReturn(false);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> itemRequestServiceImpl.getAllPageable(1L, 1, 3));
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testGetAllPageableEmpty() {
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(itemRepository.findAllByRequestIdIn((Collection<Long>) any())).thenReturn(new ArrayList<>());
        assertTrue(itemRequestServiceImpl.getAllPageable(1L, 1, 0).isEmpty());
    }
}

