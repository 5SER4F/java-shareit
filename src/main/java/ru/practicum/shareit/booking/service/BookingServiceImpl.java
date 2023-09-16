package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.exceptions.FailedBookingException;
import ru.practicum.shareit.exception.exceptions.NotOwnerException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.exception.exceptions.SelfBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final Map<String, Predicate<Booking>> states = initStates();

    @Override
    @Transactional
    public Booking postBooking(Long bookerId, BookingReceiveDto booking) {
        Item itemToBook = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> {
                    throw new ResourceNotFoundException("Предмет не существует id="
                            + booking.getItemId());
                });
        if (!itemToBook.isAvailable()
                || booking.getStart().isAfter(booking.getEnd())
                || booking.getStart().isEqual(booking.getEnd())) {
            throw new FailedBookingException("Предмет уже забранирован id=" + booking.getItemId());
        }
        User booker = userRepository.findById(bookerId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Пользователь не существует id=" + bookerId);
        });
        if (Objects.equals(itemToBook.getOwnerId(), bookerId)) {
            throw new SelfBookingException("Попытка арендовать свой предмет");
        }
        Booking newBooking = BookingMapper.receiveDtoToModel(booking);
        newBooking.setItem(itemToBook);
        newBooking.setBooker(booker);
        bookingRepository.save(newBooking);
        return newBooking;
    }

    @Override
    @Transactional
    public Booking approveBooking(Long bookingId, Long ownerId, Boolean isApproved) {
        Booking bookingToApprove = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Бронирование не найдено id=" + bookingId);
        });
        if (!ownerId.equals(bookingToApprove.getItem().getOwnerId())) {
            throw new NotOwnerException("Подтвердить бронированиие может только владелец");
        }
        if (bookingToApprove.getStatus().equals(Status.APPROVED)) {
            throw new FailedBookingException("Бронирование уже подтверждено");
        }
        bookingToApprove.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        bookingRepository.save(bookingToApprove);
        return bookingToApprove;
    }

    @Override
    @Transactional
    public Booking getBooking(Long bookingId, Long requesterId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new ResourceNotFoundException("Бронирование не найдено id=" + bookingId);
        });
        if (!Objects.equals(booking.getBooker().getId(), requesterId)
                && !Objects.equals(booking.getItem().getOwnerId(), requesterId)) {
            throw new ResourceNotFoundException("Запросить бронирование может только автор или владелец вещи");
        }
        return booking;
    }

    @Override
    @Transactional
    public List<Booking> getAllByBookerWithState(Long requesterId, String state) {
        if (!states.containsKey(state.toUpperCase())) {
            throw new FailedBookingException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(requesterId)) {
            throw new ResourceNotFoundException("Запрос бронирований несуществующего пользователя id="
                    + requesterId);
        }
        return bookingRepository.findByBookerIdOrderByStartDesc(requesterId).stream()
                .filter(states.get(state.toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Booking> getAllByItemOwnerWithState(Long itemOwnerId, String state) {
        if (!states.containsKey(state.toUpperCase())) {
            throw new FailedBookingException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (!userRepository.existsById(itemOwnerId)) {
            throw new ResourceNotFoundException("Запрос бронирований несуществующего владельца id="
                    + itemOwnerId);
        }
        return bookingRepository.findByItemOwner(itemOwnerId).stream()
                .filter(states.get(state.toUpperCase()))
                .collect(Collectors.toList());
    }

    private Map<String, Predicate<Booking>> initStates() {
        Map<String, Predicate<Booking>> states = new HashMap<>();
        states.put("ALL", booking -> true);
        states.put("CURRENT", booking -> (booking.getStart().before(now()) && booking.getEnd().after(now())));
        states.put("PAST", booking -> booking.getEnd().before(now()));
        states.put("FUTURE", booking -> booking.getStart().after(now()));
        states.put("WAITING", booking -> booking.getStatus().equals(Status.WAITING));
        states.put("REJECTED", booking -> booking.getStatus().equals(Status.REJECTED));
        return states;

    }

    private static Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}
