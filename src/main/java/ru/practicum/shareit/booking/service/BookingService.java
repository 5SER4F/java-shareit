package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking postBooking(Long bookerId, BookingReceiveDto booking);

    Booking approveBooking(Long bookingId, Long ownerId, Boolean isApproved);

    Booking getBooking(Long bookingId, Long requesterId);

    List<Booking> getAllByBookerWithState(Long requesterId, String state);

    List<Booking> getAllByItemOwnerWithState(Long requesterId, String state);
}
