package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Map;

public interface ItemRequestService {
    ItemRequest addPost(Long requesterId, Map<String, String> requestContent);

    List<ItemRequest> getAllUserRequests(Long requesterId);

    List<ItemRequest> getAllPageable(Long requesterId, int from, int size);

    ItemRequest getById(Long requesterId, Long requestId);
}
