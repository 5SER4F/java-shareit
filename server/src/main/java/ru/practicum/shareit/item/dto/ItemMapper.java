package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.dto.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {
    public static ItemDto itemToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .requestId(item.getRequestId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static Item dtoToItem(ItemDto itemDto, long ownerId) {
        return Item.builder()
                .id(itemDto.getId())
                .ownerId(ownerId)
                .requestId(itemDto.getRequestId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.isAvailable())
                .build();
    }

    public static ItemDto itemToDtoWithBooking(Item item) {
        ItemDto itemDto = itemToDto(item);
        itemDto.setLastBooking(BookingMapper.modelToSendDtoForItemDto(item.getLastBooking()));
        itemDto.setNextBooking(BookingMapper.modelToSendDtoForItemDto(item.getNextBooking()));
        itemDto.setComments(item.getComments() != null ? item.getComments().stream()
                .map(CommentMapper::modelToDto)
                .collect(Collectors.toList()) : Collections.emptyList());

        return itemDto;
    }
}
