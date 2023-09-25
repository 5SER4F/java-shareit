package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.exceptions.BadRequestException;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId,
                                              @RequestBody Map<String, String> requestContent) {
        return itemRequestClient.postRequest(requesterId, requestContent);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @NotNull
                                                 Long requesterId,
                                                 @PathVariable("requestId") Long requestId) {
        return itemRequestClient.getRequestById(requesterId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-Sharer-User-Id") @NotNull Long requesterId) {
        return itemRequestClient.getUserRequests(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllPageable(@RequestHeader("X-Sharer-User-Id")
                                                 @NotNull Long requesterId,
                                                 @RequestParam(value = "from",
                                                         defaultValue = "0")
                                                 int from,
                                                 @RequestParam(value = "size",
                                                         defaultValue = "30")
                                                 int size) {
        if (from < 0 || size <= 0) {
            throw new BadRequestException("Некорректные параметры запроса");
        }
        return itemRequestClient.getAllPageable(requesterId, from, size);
    }
}
