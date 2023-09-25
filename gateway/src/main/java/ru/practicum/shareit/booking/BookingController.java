package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingReceiveDto;
import ru.practicum.shareit.exception.exceptions.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> postBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                              @RequestBody @Valid BookingReceiveDto bookingReceiveDto) {
        return bookingClient.bookItem(bookerId, bookingReceiveDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable("bookingId") Long bookingId,
                                                 @RequestParam("approved") Boolean isApproved,
                                                 @RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId) {
        return bookingClient.approveBooking(ownerId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return bookingClient.getBooking(requesterId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerWithState(@RequestHeader("X-Sharer-User-Id") @NotNull
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
        return bookingClient.getBookings(requesterId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemOwnerWithState(@RequestParam(name = "state", defaultValue = "ALL")
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
        return bookingClient.getAllByItemOwnerWithState(state, requesterId, from, size);
    }
}
