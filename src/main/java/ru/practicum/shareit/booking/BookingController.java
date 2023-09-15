package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.booking.dto.BookingSendDto;
import ru.practicum.shareit.booking.service.BookingService;

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
    ResponseEntity<BookingSendDto> postBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                               @RequestBody @Valid BookingReceiveDto bookingReceiveDto) {
        return new ResponseEntity<>(modelToSendDto(service.postBooking(bookerId, bookingReceiveDto)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    ResponseEntity<BookingSendDto> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                  @RequestParam("approved") Boolean isApproved,
                                                  @RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId) {
        return new ResponseEntity<>(modelToSendDto(service.approveBooking(bookingId, ownerId, isApproved)),
                HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    ResponseEntity<BookingSendDto> getBooking(@PathVariable Long bookingId,
                                              @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return new ResponseEntity<>(modelToSendDto(service.getBooking(bookingId, requesterId)),
                HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<BookingSendDto>> getAllByBookerWithState
            (@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
             @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return new ResponseEntity<>(service.getAllByBookerWithState(requesterId, state).stream()
                .map(BookingMapper::modelToSendDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/owner")
    ResponseEntity<List<BookingSendDto>> getAllByItemOwnerWithState
            (@RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
             @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return new ResponseEntity<>(service.getAllByItemOwnerWithState(requesterId, state).stream()
                .map(BookingMapper::modelToSendDto)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }
}
