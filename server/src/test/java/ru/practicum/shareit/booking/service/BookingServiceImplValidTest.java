package ru.practicum.shareit.booking.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exception.exceptions.SelfBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplValidTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void testGetAllByItemOwnerWithStateNonExistingOwnerThrowsResourceNotFoundException() {
        Long itemOwnerId = 1L;
        String state = "CURRENT";
        int from = 0;
        int size = 10;

        when(userRepository.existsById(itemOwnerId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.getAllByItemOwnerWithState(itemOwnerId, state, from, size);
        });
    }

    @Test
    public void testGetAllByItemOwnerWithStateValidParametersReturnsListOfBookings() {
        Long itemOwnerId = 1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        when(userRepository.existsById(itemOwnerId)).thenReturn(true);

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        bookings.add(booking1);
        bookings.add(booking2);

        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(any(Long.class), any(Pageable.class)))
                .thenReturn(bookings);


        List<Booking> result = bookingService.getAllByItemOwnerWithState(itemOwnerId, state, from, size);

        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
    }

    @Test
    public void testGetAllByBookerWithStateNonExistingUserThrowsResourceNotFoundException() {
        Long requesterId = 1L;
        String state = "CURRENT";
        int from = 0;
        int size = 10;

        when(userRepository.existsById(requesterId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getAllByBookerWithState(requesterId, state, from, size));
    }

    @Test
    public void testGetAllByBookerWithStateValidParametersReturnsListOfBookings() {
        Long requesterId = 1L;
        String state = "ALL";
        int from = 0;
        int size = 10;

        when(userRepository.existsById(requesterId)).thenReturn(true);

        List<Booking> bookings = new ArrayList<>();
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        bookings.add(booking1);
        bookings.add(booking2);

        when(bookingRepository.findByBookerIdOrderByStartDesc(any(), any())).thenReturn(bookings);
        List<Booking> result = bookingService.getAllByBookerWithState(requesterId, state, from, size);

        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
    }

    @Test
    public void testPostBookingFrstResourceNotFoundException() {
        when((itemRepository.findById(any(Long.class)))).thenReturn(Optional.empty());
        BookingReceiveDto bookingReceiveDto = new BookingReceiveDto();
        bookingReceiveDto.setItemId(1L);
        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.postBooking(1L, bookingReceiveDto);
        });
    }

    @Test
    public void testPostBookingScndResourceNotFoundException() {
        Item item = new Item();
        item.setAvailable(true);
        when((itemRepository.findById(any(Long.class)))).thenReturn(Optional.of(item));

        BookingReceiveDto bookingReceiveDto = new BookingReceiveDto();
        bookingReceiveDto.setItemId(1L);
        bookingReceiveDto.setStart(LocalDateTime.now());
        bookingReceiveDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingReceiveDto.setItemId(1L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.postBooking(1L, bookingReceiveDto);
        });
    }

    @Test
    public void testPostBookingSelfBookingException() {
        Item itemToBook = new Item();
        itemToBook.setAvailable(true);
        itemToBook.setOwnerId(1L);
        when((itemRepository.findById(any(Long.class)))).thenReturn(Optional.of(itemToBook));

        BookingReceiveDto bookingReceiveDto = new BookingReceiveDto();
        bookingReceiveDto.setItemId(1L);
        bookingReceiveDto.setStart(LocalDateTime.now());
        bookingReceiveDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingReceiveDto.setItemId(1L);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));

        assertThrows(SelfBookingException.class, () -> {
            bookingService.postBooking(1L, bookingReceiveDto);
        });
    }

    @Test
    public void testGetBookingNonExistingBookingThrowsResourceNotFoundException() {
        Long bookingId = 1L;
        Long requesterId = 2L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBooking(bookingId, requesterId));
    }

    @Test
    public void testGetBookingUnauthorizedRequestThrowsResourceNotFoundException() {
        Long bookingId = 1L;
        Long requesterId = 2L;
        Booking booking = new Booking();
        User booker = new User();
        User itemOwner = new User();
        Item item = new Item();
        item.setOwnerId(7L);
        booker.setId(3L);
        itemOwner.setId(4L);
        booking.setBooker(booker);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBooking(bookingId, requesterId));
    }

    @Test
    public void testPostBookingValidBookingReceiveDtoReturnsBooking() {
        Long bookerId = 2L;
        BookingReceiveDto bookingReceiveDto = new BookingReceiveDto();
        bookingReceiveDto.setItemId(1L);
        bookingReceiveDto.setStart(LocalDateTime.now().plusDays(1));
        bookingReceiveDto.setEnd(LocalDateTime.now().plusDays(2));

        Item item = new Item();
        item.setId(bookingReceiveDto.getItemId());
        item.setAvailable(true);

        User booker = new User();
        booker.setId(bookerId);

        Booking newBooking = BookingMapper.receiveDtoToModel(bookingReceiveDto);
        newBooking.setItem(item);
        newBooking.setBooker(booker);

        when(itemRepository.findById(bookingReceiveDto.getItemId())).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);

        Booking result = bookingService.postBooking(bookerId, bookingReceiveDto);

        assertNotNull(result);
        assertEquals(newBooking, result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }


    @Test
    public void testApproveBookingNotOwnerThrowsNotOwnerException() {
        Long bookingId = 1L;
        Long ownerId = 2L;
        Boolean isApproved = true;

        Booking bookingToApprove = new Booking();
        bookingToApprove.setItem(new Item());
        bookingToApprove.setStatus(Status.WAITING);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingToApprove));

        assertThrows(NotOwnerException.class, () -> {
            bookingService.approveBooking(bookingId, ownerId, isApproved);
        });
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testApproveBookingAlreadyApprovedThrowsFailedBookingException() {
        Long bookingId = 1L;
        Long ownerId = 2L;
        Boolean isApproved = true;

        Booking bookingToApprove = new Booking();
        bookingToApprove.setItem(new Item());
        bookingToApprove.setStatus(Status.APPROVED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(bookingToApprove));

        assertThrows(NotOwnerException.class, () -> {
            bookingService.approveBooking(bookingId, ownerId, isApproved);
        });
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testApproveBookingInvalidBookingThrowsResourceNotFoundException() {
        Long bookingId = 1L;
        Long ownerId = 2L;
        Boolean isApproved = true;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.approveBooking(bookingId, ownerId, isApproved);
        });
        verify(bookingRepository, never()).save(any(Booking.class));
    }


}