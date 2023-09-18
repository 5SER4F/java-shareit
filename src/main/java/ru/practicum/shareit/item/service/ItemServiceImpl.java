package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static ru.practicum.shareit.item.dto.ItemMapper.dtoToItem;
import static ru.practicum.shareit.util.Patcher.setIfNotNull;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item addItem(Long ownerId, ItemDto itemDto) {
        return itemRepository.save(validateAndMap(ownerId, itemDto));
    }

    @Override
    @Transactional
    public Item patchItem(Long ownerId, Long itemId, ItemDto itemDto) {
        Item oldItem = getItemById(itemId, ownerId);
        if (!oldItem.getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException("Вещь может обновить только ее владелец");
        }
        setIfNotNull(oldItem::setName, itemDto.getName());
        setIfNotNull(oldItem::setDescription, itemDto.getDescription());
        setIfNotNull(oldItem::setAvailable, itemDto.isAvailable());
        return itemRepository.save(oldItem);
    }

    @Override
    @Transactional
    public Item getItemById(Long itemId, Long requesterId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new ResourceNotFoundException(
                    String.format("Вещь не найден id=%d", itemId)
            );
        });
        setComments(item);
        if (!Objects.equals(item.getOwnerId(), requesterId)) {
            return item;
        }
        List<Booking> bookings = bookingRepository.findByItemOwner(item.getOwnerId());
        setLastAndNextBooking(bookings, item);
        return item;
    }

    @Override
    @Transactional
    public List<Item> getAllUserItem(Long ownerId) {
        userValidation(ownerId);
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        if (items.isEmpty() || !Objects.equals(ownerId, items.get(0).getOwnerId())) {
            return items;
        }
        List<Booking> bookings = bookingRepository.findByItemOwner(items.get(0).getOwnerId());
        items.forEach(item -> setLastAndNextBooking(bookings, item));
        items.forEach(this::setComments);
        return items;
    }

    @Override
    @Transactional
    public List<Item> findByTextQuery(String query) {
        if ("".equals(query)) {
            return Collections.emptyList();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(query, query);
    }

    @Override
    @Transactional
    public Comment addComment(Long requesterId, Long itemId, Map<String, String> text) {
        if (text.isEmpty() || text.get("text").isBlank()) {
            throw new FailedCommentException("Попытка создать пустой комментарий");
        }
        if (
                !bookingRepository.existsByItemIdAndBookerIdAndEndBefore(itemId, requesterId,
                        Timestamp.from(Instant.now()))
        ) {
            throw new FailedCommentException("Попытка создать комментарий, не беря в аренду");
        }
        Comment comment = commentRepository.save(
                Comment.builder()
                        .authorId(requesterId)
                        .itemId(itemId)
                        .text(text.get("text"))
                        .created(Timestamp.from(Instant.now()))
                        .build()
        );
        comment.setAuthorName(userRepository.findById(requesterId).get().getName());
        return comment;
    }

    private void userValidation(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "Запрос от несуществующего пользователя userid=" + userId
            );
        }
    }

    private Item validateAndMap(long userId, ItemDto itemDto) {
        userValidation(userId);
        return dtoToItem(itemDto, userId);
    }

    private void setLastAndNextBooking(List<Booking> bookings, Item item) {
        Timestamp now = Timestamp.from(Instant.now());
        Optional<Booking> lastBooking = bookings.stream()
                .filter(booking -> booking.getItem().getId() == item.getId())
                .filter(booking -> !booking.getStatus().equals(Status.REJECTED))
//                .filter(booking -> booking.getEnd().before(now))
                .filter(booking -> booking.getStart().before(now))
                .max(Comparator.comparing(Booking::getStart));//min
        Optional<Booking> nextBooking = bookings.stream()
                .filter(booking -> booking.getItem().getId() == item.getId())
                .filter(booking -> !booking.getStatus().equals(Status.REJECTED))
                .filter(booking -> booking.getStart().after(now))
                .min(Comparator.comparing(Booking::getStart));//max
        item.setLastBooking(lastBooking.orElse(null));
        item.setNextBooking(nextBooking.orElse(null));
    }

    private void setComments(Item item) {
        item.setComments(commentRepository.findByItemId(item.getId()));
    }
}
