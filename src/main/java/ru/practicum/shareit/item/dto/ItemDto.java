package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
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

    public Boolean isAvailable() {
        return available;
    }
}
