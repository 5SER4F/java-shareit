package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingSendDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private long id;
    @NotBlank(message = "Имя вещи должно быть указано")
    private String name;
    @NotBlank(message = "У вещи должно быть описание")
    private String description;
    @AssertTrue
    @NotNull
    private Boolean available;
    private BookingSendDto lastBooking;
    private BookingSendDto nextBooking;
    private List<CommentDto> comments;
    private long requestId;

    public Boolean isAvailable() {
        return available;
    }
}
