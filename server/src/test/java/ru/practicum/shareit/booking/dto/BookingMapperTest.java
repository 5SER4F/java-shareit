package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingMapperTest {

    @Test
    void modelToSendDtoForItemDto_ValidBooking_ReturnsBookingSendDto() {
        Booking booking = new Booking();
        booking.setId(1L);

        User booker = new User();
        booker.setId(2L);

        booking.setBooker(booker);

        BookingSendDto result = BookingMapper.modelToSendDtoForItemDto(booking);

        assertEquals(booking.getId(), result.getId());
        assertEquals(booking.getBooker().getId(), result.getBookerId());
        assertNull(result.getStart());
        assertNull(result.getEnd());
        assertNull(result.getStatus());
        assertNull(result.getBooker());
        assertNull(result.getItem());
    }
}