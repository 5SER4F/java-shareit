package ru.practicum.shareit.request.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface RequestService {
    ItemRequest addPost(Long requesterId, Map<String, String> requestContent);

}
