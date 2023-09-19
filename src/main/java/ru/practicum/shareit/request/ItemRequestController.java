package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.exceptions.BadRequestException;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestSendDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.request.dto.ItemRequestMapper.modelToSendDto;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    ResponseEntity<ItemRequestSendDto> postRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                                   @RequestBody Map<String, String> requestContent) {
        return new ResponseEntity<>(modelToSendDto(service.addPost(requesterId, requestContent)),
                HttpStatus.CREATED);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<ItemRequestSendDto> getRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                                      @PathVariable("requestId") Long requestId) {
        return new ResponseEntity<>(modelToSendDto(service.getById(requesterId, requestId)), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<ItemRequestSendDto>> getUserRequests(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return new ResponseEntity<>(service.getAllUserRequests(requesterId).stream()
                .map(ItemRequestMapper::modelToSendDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/all")
    ResponseEntity<List<ItemRequestSendDto>> getAllPageable(@RequestHeader("X-Sharer-User-Id")
                                                            @NotNull Long requesterId,
                                                            @RequestParam(value = "from",
                                                                    required = false)
                                                            Optional<Integer> from,
                                                            @RequestParam(value = "size",
                                                                    required = false)
                                                            Optional<Integer> size) {
        if (from.isEmpty() || size.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        if (from.get() < 0 || size.get() < 0
                || (from.get() == 0 & size.get() == 0)) {
            throw new BadRequestException("Некорректные параметры запроса");
        }
        return new ResponseEntity<>(service.getAllPageable(requesterId, from.get(), size.get()).stream()
                .map(ItemRequestMapper::modelToSendDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
