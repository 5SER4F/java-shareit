package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FailedBookingException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void postBookingValidDataBookingCreated() {
        Long bookerId = 1L;
        Long itemId = 2L;
        BookingReceiveDto bookingDto = new BookingReceiveDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(Timestamp.from(Instant.now()).toLocalDateTime());
        bookingDto.setEnd(Timestamp.from(Instant.now().plusSeconds(3600)).toLocalDateTime());

        Item item = new Item();
        item.setId(itemId);
        item.setOwnerId(3L);
        item.setAvailable(true);

        User booker = new User();
        booker.setId(bookerId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking savedBooking = invocation.getArgument(0);
            savedBooking.setId(4L);
            return savedBooking;
        });

        Booking result = bookingService.postBooking(bookerId, bookingDto);

        assertNotNull(result);
        assertEquals(itemId, result.getItem().getId());
        assertEquals(bookerId, result.getBooker().getId());
        assertEquals(Status.WAITING, result.getStatus());

        verify(itemRepository, times(1)).findById(itemId);
        verify(userRepository, times(1)).findById(bookerId);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void postBookingItemNotFoundThrowResourceNotFoundException() {
        Long bookerId = 1L;
        Long itemId = 2L;
        BookingReceiveDto bookingDto = new BookingReceiveDto();
        bookingDto.setItemId(itemId);
        bookingDto.setStart(Timestamp.from(Instant.now()).toLocalDateTime());
        bookingDto.setEnd(Timestamp.from(Instant.now().plusSeconds(3600)).toLocalDateTime());

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.postBooking(bookerId, bookingDto));

        verify(itemRepository, times(1)).findById(itemId);
        verify(userRepository, never()).findById(any(Long.class));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void postBookingShouldThrowResourceNotFoundExceptionWhenItemNotFound() {
        BookingReceiveDto bookingDto = new BookingReceiveDto();
        bookingDto.setItemId(1L);

        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.postBooking(1L, bookingDto);
        });

        verify(itemRepository, times(1)).findById(bookingDto.getItemId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void postBookingShouldThrowFailedBookingExceptionWhenItemAlreadyBooked() {
        BookingReceiveDto bookingDto = new BookingReceiveDto();
        bookingDto.setItemId(1L);

        Item item = new Item();
        item.setAvailable(false);

        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));

        assertThrows(FailedBookingException.class, () -> {
            bookingService.postBooking(1L, bookingDto);
        });

        verify(itemRepository, times(1)).findById(bookingDto.getItemId());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

}