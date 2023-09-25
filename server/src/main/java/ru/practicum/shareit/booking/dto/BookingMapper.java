package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.sql.Timestamp;

@UtilityClass
public class BookingMapper {

    public static Booking receiveDtoToModel(BookingReceiveDto b) {
        if (b == null) {
            return null;
        }
        return Booking.builder()
                .start(Timestamp.valueOf(b.getStart()))
                .end(Timestamp.valueOf(b.getEnd()))
                .status(Status.WAITING)
                .build();
    }

    public static BookingSendDto modelToSendDto(Booking b) {
        if (b == null) {
            return null;
        }
        return BookingSendDto.builder()
                .id(b.getId())
                .start(b.getStart().toLocalDateTime())
                .end(b.getEnd().toLocalDateTime())
                .status(b.getStatus())
                .booker(b.getBooker())
                .item(ItemMapper.itemToDto(b.getItem()))
                .build();
    }

    public static BookingSendDto modelToSendDtoForItemDto(Booking b) {
        if (b == null) {
            return null;
        }
        return BookingSendDto.builder()
                .id(b.getId())
                .bookerId(b.getBooker().getId())
                .build();
    }
}
