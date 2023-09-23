package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingSendDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private User booker;
    private Long bookerId;
    private ItemDto item;
}
