package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FailedCommentException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.comment.Comment;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.storage.comment.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ItemServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testAddItem() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Item item = new Item();
        item.setAvailable(true);
        item.setComments(new ArrayList<>());
        item.setDescription("something");
        item.setId(1L);
        item.setLastBooking(new Booking());
        item.setName("Name");
        item.setNextBooking(new Booking());
        item.setOwnerId(1L);
        item.setRequestId(1L);

        Booking booking = new Booking();
        booking.setBooker(user1);
        booking.setEnd(mock(Timestamp.class));
        booking.setId(1L);
        booking.setItem(item);
        booking.setStart(mock(Timestamp.class));
        booking.setStatus(Status.WAITING);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setComments(new ArrayList<>());
        item1.setDescription("something");
        item1.setId(1L);
        item1.setLastBooking(new Booking());
        item1.setName("Name");
        item1.setNextBooking(new Booking());
        item1.setOwnerId(1L);
        item1.setRequestId(1L);

        Booking booking1 = new Booking();
        booking1.setBooker(user2);
        booking1.setEnd(mock(Timestamp.class));
        booking1.setId(1L);
        booking1.setItem(item1);
        booking1.setStart(mock(Timestamp.class));
        booking1.setStatus(Status.WAITING);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setComments(new ArrayList<>());
        item2.setDescription("something");
        item2.setId(1L);
        item2.setLastBooking(booking);
        item2.setName("Name");
        item2.setNextBooking(booking1);
        item2.setOwnerId(1L);
        item2.setRequestId(1L);

        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setEnd(mock(Timestamp.class));
        booking2.setId(1L);
        booking2.setItem(item2);
        booking2.setStart(mock(Timestamp.class));
        booking2.setStatus(Status.WAITING);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        Item item3 = new Item();
        item3.setAvailable(true);
        item3.setComments(new ArrayList<>());
        item3.setDescription("something");
        item3.setId(1L);
        item3.setLastBooking(new Booking());
        item3.setName("Name");
        item3.setNextBooking(new Booking());
        item3.setOwnerId(1L);
        item3.setRequestId(1L);

        Booking booking3 = new Booking();
        booking3.setBooker(user4);
        booking3.setEnd(mock(Timestamp.class));
        booking3.setId(1L);
        booking3.setItem(item3);
        booking3.setStart(mock(Timestamp.class));
        booking3.setStatus(Status.WAITING);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Item item4 = new Item();
        item4.setAvailable(true);
        item4.setComments(new ArrayList<>());
        item4.setDescription("something");
        item4.setId(1L);
        item4.setLastBooking(new Booking());
        item4.setName("Name");
        item4.setNextBooking(new Booking());
        item4.setOwnerId(1L);
        item4.setRequestId(1L);

        Booking booking4 = new Booking();
        booking4.setBooker(user5);
        booking4.setEnd(mock(Timestamp.class));
        booking4.setId(1L);
        booking4.setItem(item4);
        booking4.setStart(mock(Timestamp.class));
        booking4.setStatus(Status.WAITING);

        Item item5 = new Item();
        item5.setAvailable(true);
        item5.setComments(new ArrayList<>());
        item5.setDescription("something");
        item5.setId(1L);
        item5.setLastBooking(booking3);
        item5.setName("Name");
        item5.setNextBooking(booking4);
        item5.setOwnerId(1L);
        item5.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user3);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item5);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);

        Item item6 = new Item();
        item6.setAvailable(true);
        item6.setComments(new ArrayList<>());
        item6.setDescription("something");
        item6.setId(1L);
        item6.setLastBooking(booking2);
        item6.setName("Name");
        item6.setNextBooking(booking5);
        item6.setOwnerId(1L);
        item6.setRequestId(1L);
        when(itemRepository.save((Item) any())).thenReturn(item6);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertSame(item6, itemServiceImpl.addItem(1L, new ItemDto()));
        verify(itemRepository).save((Item) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testAddItemResourceNotFoundException() {
        when(itemRepository.save((Item) any())).thenThrow(new ResourceNotFoundException("error"));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ResourceNotFoundException.class, () -> itemServiceImpl.addItem(1L, new ItemDto()));
        verify(itemRepository).save((Item) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testPatchItem() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setEnd(mock(Timestamp.class));
        booking.setId(1L);
        booking.setItem(new Item());
        booking.setStart(mock(Timestamp.class));
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setBooker(new User());
        booking1.setEnd(mock(Timestamp.class));
        booking1.setId(1L);
        booking1.setItem(new Item());
        booking1.setStart(mock(Timestamp.class));
        booking1.setStatus(Status.WAITING);

        Item item = new Item();
        item.setAvailable(true);
        item.setComments(new ArrayList<>());
        item.setDescription("something");
        item.setId(1L);
        item.setLastBooking(booking);
        item.setName("Name");
        item.setNextBooking(booking1);
        item.setOwnerId(1L);
        item.setRequestId(1L);

        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setEnd(mock(Timestamp.class));
        booking2.setId(1L);
        booking2.setItem(item);
        booking2.setStart(mock(Timestamp.class));
        booking2.setStatus(Status.WAITING);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Booking booking3 = new Booking();
        booking3.setBooker(new User());
        booking3.setEnd(mock(Timestamp.class));
        booking3.setId(1L);
        booking3.setItem(new Item());
        booking3.setStart(mock(Timestamp.class));
        booking3.setStatus(Status.WAITING);

        Booking booking4 = new Booking();
        booking4.setBooker(new User());
        booking4.setEnd(mock(Timestamp.class));
        booking4.setId(1L);
        booking4.setItem(new Item());
        booking4.setStart(mock(Timestamp.class));
        booking4.setStatus(Status.WAITING);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setComments(new ArrayList<>());
        item1.setDescription("something");
        item1.setId(1L);
        item1.setLastBooking(booking3);
        item1.setName("Name");
        item1.setNextBooking(booking4);
        item1.setOwnerId(1L);
        item1.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user1);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item1);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setComments(new ArrayList<>());
        item2.setDescription("something");
        item2.setId(1L);
        item2.setLastBooking(booking2);
        item2.setName("Name");
        item2.setNextBooking(booking5);
        item2.setOwnerId(1L);
        item2.setRequestId(1L);
        Optional<Item> ofResult = Optional.of(item2);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        Item item3 = new Item();
        item3.setAvailable(true);
        item3.setComments(new ArrayList<>());
        item3.setDescription("something");
        item3.setId(1L);
        item3.setLastBooking(new Booking());
        item3.setName("Name");
        item3.setNextBooking(new Booking());
        item3.setOwnerId(1L);
        item3.setRequestId(1L);

        Booking booking6 = new Booking();
        booking6.setBooker(user3);
        booking6.setEnd(mock(Timestamp.class));
        booking6.setId(1L);
        booking6.setItem(item3);
        booking6.setStart(mock(Timestamp.class));
        booking6.setStatus(Status.WAITING);

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

        Item item4 = new Item();
        item4.setAvailable(true);
        item4.setComments(new ArrayList<>());
        item4.setDescription("something");
        item4.setId(1L);
        item4.setLastBooking(new Booking());
        item4.setName("Name");
        item4.setNextBooking(new Booking());
        item4.setOwnerId(1L);
        item4.setRequestId(1L);

        Booking booking7 = new Booking();
        booking7.setBooker(user4);
        booking7.setEnd(mock(Timestamp.class));
        booking7.setId(1L);
        booking7.setItem(item4);
        booking7.setStart(mock(Timestamp.class));
        booking7.setStatus(Status.WAITING);

        Item item5 = new Item();
        item5.setAvailable(true);
        item5.setComments(new ArrayList<>());
        item5.setDescription("something");
        item5.setId(1L);
        item5.setLastBooking(booking6);
        item5.setName("Name");
        item5.setNextBooking(booking7);
        item5.setOwnerId(1L);
        item5.setRequestId(1L);

        Booking booking8 = new Booking();
        booking8.setBooker(user2);
        booking8.setEnd(mock(Timestamp.class));
        booking8.setId(1L);
        booking8.setItem(item5);
        booking8.setStart(mock(Timestamp.class));
        booking8.setStatus(Status.WAITING);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        User user6 = new User();
        user6.setEmail("jane.doe@example.org");
        user6.setId(1L);
        user6.setName("Name");

        Item item6 = new Item();
        item6.setAvailable(true);
        item6.setComments(new ArrayList<>());
        item6.setDescription("something");
        item6.setId(1L);
        item6.setLastBooking(new Booking());
        item6.setName("Name");
        item6.setNextBooking(new Booking());
        item6.setOwnerId(1L);
        item6.setRequestId(1L);

        Booking booking9 = new Booking();
        booking9.setBooker(user6);
        booking9.setEnd(mock(Timestamp.class));
        booking9.setId(1L);
        booking9.setItem(item6);
        booking9.setStart(mock(Timestamp.class));
        booking9.setStatus(Status.WAITING);

        User user7 = new User();
        user7.setEmail("jane.doe@example.org");
        user7.setId(1L);
        user7.setName("Name");

        when(itemRepository.save((Item) any())).thenReturn(item6);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        when(bookingRepository.findByItemOwner((Long) any())).thenReturn(new ArrayList<>());
        when(commentRepository.findByItemId((Long) any())).thenReturn(new ArrayList<>());
        assertSame(item6, itemServiceImpl.patchItem(1L, 1L, new ItemDto()));
        verify(itemRepository).save((Item) any());
        verify(itemRepository).findById((Long) any());
        verify(bookingRepository).findByItemOwner((Long) any());
        verify(commentRepository).findByItemId((Long) any());
    }

    @Test
    void testPatchItemResourceNotFoundException() {
        when(commentRepository.findByItemId((Long) any())).thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemServiceImpl.patchItem(1L, 1L, new ItemDto()));
    }

    @Test
    void testGetItemById() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setEnd(mock(Timestamp.class));
        booking.setId(1L);
        booking.setItem(new Item());
        booking.setStart(mock(Timestamp.class));
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setBooker(new User());
        booking1.setEnd(mock(Timestamp.class));
        booking1.setId(1L);
        booking1.setItem(new Item());
        booking1.setStart(mock(Timestamp.class));
        booking1.setStatus(Status.WAITING);

        Item item = new Item();
        item.setAvailable(true);
        item.setComments(new ArrayList<>());
        item.setDescription("something");
        item.setId(1L);
        item.setLastBooking(booking);
        item.setName("Name");
        item.setNextBooking(booking1);
        item.setOwnerId(1L);
        item.setRequestId(1L);

        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setEnd(mock(Timestamp.class));
        booking2.setId(1L);
        booking2.setItem(item);
        booking2.setStart(mock(Timestamp.class));
        booking2.setStatus(Status.WAITING);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Booking booking3 = new Booking();
        booking3.setBooker(new User());
        booking3.setEnd(mock(Timestamp.class));
        booking3.setId(1L);
        booking3.setItem(new Item());
        booking3.setStart(mock(Timestamp.class));
        booking3.setStatus(Status.WAITING);

        Booking booking4 = new Booking();
        booking4.setBooker(new User());
        booking4.setEnd(mock(Timestamp.class));
        booking4.setId(1L);
        booking4.setItem(new Item());
        booking4.setStart(mock(Timestamp.class));
        booking4.setStatus(Status.WAITING);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setComments(new ArrayList<>());
        item1.setDescription("something");
        item1.setId(1L);
        item1.setLastBooking(booking3);
        item1.setName("Name");
        item1.setNextBooking(booking4);
        item1.setOwnerId(1L);
        item1.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user1);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item1);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);

        Item item2 = new Item();
        item2.setAvailable(true);
        ArrayList<Comment> commentList = new ArrayList<>();
        item2.setComments(commentList);
        item2.setDescription("something");
        item2.setId(1L);
        item2.setLastBooking(booking2);
        item2.setName("Name");
        item2.setNextBooking(booking5);
        item2.setOwnerId(1L);
        item2.setRequestId(1L);
        Optional<Item> ofResult = Optional.of(item2);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        when(bookingRepository.findByItemOwner((Long) any())).thenReturn(new ArrayList<>());
        when(commentRepository.findByItemId((Long) any())).thenReturn(new ArrayList<>());
        Item actualItemById = itemServiceImpl.getItemById(1L, 1L);
        assertSame(item2, actualItemById);
        assertNull(actualItemById.getNextBooking());
        assertNull(actualItemById.getLastBooking());
        assertEquals(commentList, actualItemById.getComments());
        verify(itemRepository).findById((Long) any());
        verify(bookingRepository).findByItemOwner((Long) any());
        verify(commentRepository).findByItemId((Long) any());
    }

    @Test
    void testGetItemByIdResourceNotFoundException() {
        when(commentRepository.findByItemId((Long) any())).thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemServiceImpl.getItemById(1L, 1L));
    }

    @Test
    void testGetAllUserItem() {
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemRepository.findByOwnerId((Long) any())).thenReturn(itemList);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        List<Item> actualAllUserItem = itemServiceImpl.getAllUserItem(1L);
        assertSame(itemList, actualAllUserItem);
        assertTrue(actualAllUserItem.isEmpty());
        verify(itemRepository).findByOwnerId((Long) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testGetAllUserItemResourceNotFoundException() {
        when(itemRepository.findByOwnerId((Long) any())).thenThrow(new ResourceNotFoundException("error"));
        when(userRepository.existsById((Long) any())).thenReturn(true);
        assertThrows(ResourceNotFoundException.class, () -> itemServiceImpl.getAllUserItem(1L));
        verify(itemRepository).findByOwnerId((Long) any());
        verify(userRepository).existsById((Long) any());
    }

    @Test
    void testGetAllUserItemNothing() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");

        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setEnd(mock(Timestamp.class));
        booking.setId(1L);
        booking.setItem(new Item());
        booking.setStart(mock(Timestamp.class));
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setBooker(new User());
        booking1.setEnd(mock(Timestamp.class));
        booking1.setId(1L);
        booking1.setItem(new Item());
        booking1.setStart(mock(Timestamp.class));
        booking1.setStatus(Status.WAITING);

        Item item = new Item();
        item.setAvailable(true);
        item.setComments(new ArrayList<>());
        item.setDescription("something");
        item.setId(1L);
        item.setLastBooking(booking);
        item.setName("Name");
        item.setNextBooking(booking1);
        item.setOwnerId(1L);
        item.setRequestId(1L);

        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setEnd(mock(Timestamp.class));
        booking2.setId(1L);
        booking2.setItem(item);
        booking2.setStart(mock(Timestamp.class));
        booking2.setStatus(Status.WAITING);

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Booking booking3 = new Booking();
        booking3.setBooker(new User());
        booking3.setEnd(mock(Timestamp.class));
        booking3.setId(1L);
        booking3.setItem(new Item());
        booking3.setStart(mock(Timestamp.class));
        booking3.setStatus(Status.WAITING);

        Booking booking4 = new Booking();
        booking4.setBooker(new User());
        booking4.setEnd(mock(Timestamp.class));
        booking4.setId(1L);
        booking4.setItem(new Item());
        booking4.setStart(mock(Timestamp.class));
        booking4.setStatus(Status.WAITING);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setComments(new ArrayList<>());
        item1.setDescription("something");
        item1.setId(1L);
        item1.setLastBooking(booking3);
        item1.setName("Name");
        item1.setNextBooking(booking4);
        item1.setOwnerId(1L);
        item1.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user1);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item1);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);
        Item item2 = mock(Item.class);
        when(item2.getId()).thenReturn(1L);
        when(item2.getOwnerId()).thenReturn(1L);
        doNothing().when(item2).setAvailable((Boolean) any());
        doNothing().when(item2).setComments((List<Comment>) any());
        doNothing().when(item2).setDescription((String) any());
        doNothing().when(item2).setId(anyLong());
        doNothing().when(item2).setLastBooking((Booking) any());
        doNothing().when(item2).setName((String) any());
        doNothing().when(item2).setNextBooking((Booking) any());
        doNothing().when(item2).setOwnerId((Long) any());
        doNothing().when(item2).setRequestId((Long) any());
        item2.setAvailable(true);
        item2.setComments(new ArrayList<>());
        item2.setDescription("something");
        item2.setId(1L);
        item2.setLastBooking(booking2);
        item2.setName("Name");
        item2.setNextBooking(booking5);
        item2.setOwnerId(1L);
        item2.setRequestId(1L);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item2);
        when(itemRepository.findByOwnerId((Long) any())).thenReturn(itemList);
        when(userRepository.existsById((Long) any())).thenReturn(true);
        when(bookingRepository.findByItemOwner((Long) any())).thenReturn(new ArrayList<>());
        when(commentRepository.findByItemId((Long) any())).thenReturn(new ArrayList<>());
        List<Item> actualAllUserItem = itemServiceImpl.getAllUserItem(1L);
        assertSame(itemList, actualAllUserItem);
        assertEquals(1, actualAllUserItem.size());
        verify(itemRepository).findByOwnerId((Long) any());
        verify(item2, atLeast(1)).getOwnerId();
        verify(item2).getId();
        verify(item2).setAvailable((Boolean) any());
        verify(item2, atLeast(1)).setComments(any());
        verify(item2).setDescription((String) any());
        verify(item2).setId(anyLong());
        verify(item2, atLeast(1)).setLastBooking((Booking) any());
        verify(item2).setName((String) any());
        verify(item2, atLeast(1)).setNextBooking((Booking) any());
        verify(item2).setOwnerId((Long) any());
        verify(item2).setRequestId((Long) any());
        verify(userRepository).existsById((Long) any());
        verify(bookingRepository).findByItemOwner((Long) any());
        verify(commentRepository).findByItemId((Long) any());
    }

    @Test
    void testFindByTextQuery() {
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue((String) any(), (String) any()))
                .thenReturn(itemList);
        List<Item> actualFindByTextQueryResult = itemServiceImpl.findByTextQuery("Query");
        assertSame(itemList, actualFindByTextQueryResult);
        assertTrue(actualFindByTextQueryResult.isEmpty());
        verify(itemRepository).findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue((String) any(),
                (String) any());
    }

    @Test
    void testFindByTextQueryEmpty() {
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue((String) any(), (String) any()))
                .thenReturn(new ArrayList<>());
        assertTrue(itemServiceImpl.findByTextQuery("").isEmpty());
    }

    @Test
    void testFindByTextQueryResourceNotFoundException() {
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue((String) any(), (String) any()))
                .thenThrow(new ResourceNotFoundException("error"));
        assertThrows(ResourceNotFoundException.class, () -> itemServiceImpl.findByTextQuery("Query"));
        verify(itemRepository).findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue((String) any(),
                (String) any());
    }

    @Test
    void testAddComment() {
        assertThrows(FailedCommentException.class, () -> itemServiceImpl.addComment(1L, 1L, new HashMap<>()));
    }

    @Test
    void addCommentSecFailedCommentException() {
        when(bookingRepository.existsByItemIdAndBookerIdAndEndBefore(any(), any(), any())).thenReturn(false);
        assertThrows(FailedCommentException.class, () -> itemServiceImpl.addComment(1L,
                1L, Map.of("text", "text")));
    }

    @Test
    void addComment_ValidParameters_ReturnsComment() {
        Long requesterId = 1L;
        Long itemId = 2L;
        Map<String, String> text = new HashMap<>();
        text.put("text", "This is a comment");

        when(bookingRepository.existsByItemIdAndBookerIdAndEndBefore(any(), any(), any()))
                .thenReturn(true);

        Comment comment = new Comment();
        comment.setId(3L);
        comment.setAuthorId(requesterId);
        comment.setItemId(itemId);
        comment.setText(text.get("text"));
        comment.setCreated(Timestamp.from(Instant.now()));
        User user = new User();
        user.setName("John");
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(user));

        Comment result = itemServiceImpl.addComment(requesterId, itemId, text);

        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getAuthorId(), result.getAuthorId());
        assertEquals(comment.getItemId(), result.getItemId());
        assertEquals(comment.getText(), result.getText());
        assertEquals(comment.getCreated(), result.getCreated());
        assertEquals(user.getName(), result.getAuthorName());
    }
}

