package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.dto.BookingSendDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.exceptions.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.modelToSendDto;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public ResponseEntity<BookingSendDto> postBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                                      @RequestBody @Valid BookingReceiveDto bookingReceiveDto) {
        return new ResponseEntity<>(modelToSendDto(service.postBooking(bookerId, bookingReceiveDto)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingSendDto> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                         @RequestParam("approved") Boolean isApproved,
                                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId) {
        return new ResponseEntity<>(modelToSendDto(service.approveBooking(bookingId, ownerId, isApproved)),
                HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingSendDto> getBooking(@PathVariable Long bookingId,
                                                     @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return new ResponseEntity<>(modelToSendDto(service.getBooking(bookingId, requesterId)),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingSendDto>> getAllByBookerWithState(@RequestHeader("X-Sharer-User-Id") @NotNull
                                                                        Long requesterId,
                                                                        @RequestParam(name = "state", defaultValue = "ALL")
                                                                        String state,
                                                                        @RequestParam(value = "from",
                                                                                defaultValue = "0")
                                                                        int from,
                                                                        @RequestParam(value = "size",
                                                                                defaultValue = "25")
                                                                        int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Некорректные параметры запроса");
        }
        return new ResponseEntity<>(service.getAllByBookerWithState(requesterId, state, from, size).stream()
                .map(BookingMapper::modelToSendDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingSendDto>> getAllByItemOwnerWithState(@RequestParam(name = "state", defaultValue = "ALL")
                                                                           String state,
                                                                           @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                                                           @RequestParam(value = "from",
                                                                                   defaultValue = "0")
                                                                           int from,
                                                                           @RequestParam(value = "size",
                                                                                   defaultValue = "30")
                                                                           int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Некорректные параметры запроса");
        }
        return new ResponseEntity<>(service.getAllByItemOwnerWithState(requesterId, state, from, size).stream()
                .map(BookingMapper::modelToSendDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
