package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestSendDto modelToSendDto(ItemRequest model) {
        return ItemRequestSendDto.builder()
                .id(model.getId())
                .description(model.getDescription())
                .created(model.getCreated().toLocalDateTime())
                .items(model.getItems())
                .build();
    }
}
