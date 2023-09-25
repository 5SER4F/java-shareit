package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FailedBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingServiceImpl.class})
@ExtendWith(SpringExtension.class)
class BookingServiceImplTest {
    @MockBean
    private BookingRepository bookingRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testPostBooking() {
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
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);

        BookingReceiveDto bookingReceiveDto = new BookingReceiveDto();
        bookingReceiveDto.setEnd(LocalDateTime.of(1, 1, 1, 1, 1));
        bookingReceiveDto.setItemId(1L);
        bookingReceiveDto.setStart(LocalDateTime.of(1, 1, 1, 1, 1));
        assertThrows(FailedBookingException.class, () -> bookingServiceImpl.postBooking(1L, bookingReceiveDto));
        verify(itemRepository).findById((Long) any());
    }

    @Test
    public void testApproveBooking() {
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
        Optional<Booking> ofResult = Optional.of(booking2);

        User user3 = new User();
        user3.setEmail("jane.doe@example.org");
        user3.setId(1L);
        user3.setName("Name");

        User user4 = new User();
        user4.setEmail("jane.doe@example.org");
        user4.setId(1L);
        user4.setName("Name");

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

        Item item3 = new Item();
        item3.setAvailable(true);
        item3.setComments(new ArrayList<>());
        item3.setDescription("something");
        item3.setId(1L);
        item3.setLastBooking(booking3);
        item3.setName("Name");
        item3.setNextBooking(booking4);
        item3.setOwnerId(1L);
        item3.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user4);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item3);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);

        User user5 = new User();
        user5.setEmail("jane.doe@example.org");
        user5.setId(1L);
        user5.setName("Name");

        Booking booking6 = new Booking();
        booking6.setBooker(new User());
        booking6.setEnd(mock(Timestamp.class));
        booking6.setId(1L);
        booking6.setItem(new Item());
        booking6.setStart(mock(Timestamp.class));
        booking6.setStatus(Status.WAITING);

        Booking booking7 = new Booking();
        booking7.setBooker(new User());
        booking7.setEnd(mock(Timestamp.class));
        booking7.setId(1L);
        booking7.setItem(new Item());
        booking7.setStart(mock(Timestamp.class));
        booking7.setStatus(Status.WAITING);

        Item item4 = new Item();
        item4.setAvailable(true);
        item4.setComments(new ArrayList<>());
        item4.setDescription("something");
        item4.setId(1L);
        item4.setLastBooking(booking6);
        item4.setName("Name");
        item4.setNextBooking(booking7);
        item4.setOwnerId(1L);
        item4.setRequestId(1L);

        Booking booking8 = new Booking();
        booking8.setBooker(user5);
        booking8.setEnd(mock(Timestamp.class));
        booking8.setId(1L);
        booking8.setItem(item4);
        booking8.setStart(mock(Timestamp.class));
        booking8.setStatus(Status.WAITING);

        Item item5 = new Item();
        item5.setAvailable(true);
        item5.setComments(new ArrayList<>());
        item5.setDescription("something");
        item5.setId(1L);
        item5.setLastBooking(booking5);
        item5.setName("Name");
        item5.setNextBooking(booking8);
        item5.setOwnerId(1L);
        item5.setRequestId(1L);

        Booking booking9 = new Booking();
        booking9.setBooker(user3);
        booking9.setEnd(mock(Timestamp.class));
        booking9.setId(1L);
        booking9.setItem(item5);
        booking9.setStart(mock(Timestamp.class));
        booking9.setStatus(Status.WAITING);
        when(bookingRepository.save((Booking) any())).thenReturn(booking9);
        when(bookingRepository.findById((Long) any())).thenReturn(ofResult);
        Booking actualApproveBookingResult = bookingServiceImpl.approveBooking(1L, 1L, true);
        assertSame(booking2, actualApproveBookingResult);
        assertEquals(Status.APPROVED, actualApproveBookingResult.getStatus());
        verify(bookingRepository).save((Booking) any());
        verify(bookingRepository).findById((Long) any());
    }

    @Test
    public void testGetBooking() {
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
        Optional<Booking> ofResult = Optional.of(booking2);
        when(bookingRepository.findById((Long) any())).thenReturn(ofResult);
        assertSame(booking2, bookingServiceImpl.getBooking(1L, 1L));
        verify(bookingRepository).findById((Long) any());
    }

    @Test
    public void testGetAllByBookerWithState() {
        assertThrows(FailedBookingException.class, () -> bookingServiceImpl.getAllByBookerWithState(1L, "MD", 1, 3));
    }

    @Test
    public void testGetAllByItemOwnerWithState() {
        assertThrows(FailedBookingException.class, () -> bookingServiceImpl.getAllByItemOwnerWithState(1L, "MD", 1, 3));
    }
}

