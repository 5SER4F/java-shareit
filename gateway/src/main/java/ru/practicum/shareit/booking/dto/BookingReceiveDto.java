package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingReceiveDto {
    @NotNull(message = "Не указан предмет для бронирования")
    private Long itemId;
    @Future(message = "Начало бронирования не может быть в прошлом")
    @NotNull
    private LocalDateTime start;
    @Future(message = "Конец бронирования не может быть в прошлом")
    @NotNull
    private LocalDateTime end;
}
