package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.FailedItemRequestException;
import ru.practicum.shareit.exception.exceptions.ResourceNotFoundException;
import ru.practicum.shareit.item.dto.ItemInRequestProjection;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest addPost(Long requesterId, Map<String, String> requestContent) {
        if (!userRepository.existsById(requesterId)) {
            throw new ResourceNotFoundException("Запрос от несуществующего пользователся id=" + requesterId);
        }
        if (requestContent.get("description") == null) {
            throw new FailedItemRequestException("Запрос должен содержать описание");
        }
        return itemRequestRepository.save(ItemRequest.builder()
                .requesterId(requesterId)
                .description(requestContent.get("description"))
                .created(Timestamp.from(Instant.now()))
                .build());
    }

    @Override
    public ItemRequest getById(Long requesterId, Long requestId) {
        userExist(requesterId);
        ItemRequest request = itemRequestRepository.findById(requestId).orElseThrow(() -> {
                    throw new ResourceNotFoundException("Запрос не найден");
                }
        );
        request.setItems(itemRepository.findAllByRequestIdIn(List.of(requestId)));
        return request;
    }

    @Override
    public List<ItemRequest> getAllUserRequests(Long requesterId) {
        userExist(requesterId);
        List<ItemRequest> userRequests = itemRequestRepository.findByRequesterId(requesterId);
        List<Long> requestsIds = userRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());
        List<ItemInRequestProjection> answersOnRequests = itemRepository.findAllByRequestIdIn(requestsIds);
        userRequests.forEach(itemRequest -> setItemsInRequest(itemRequest, answersOnRequests));
        return userRequests;
    }

    @Override
    public List<ItemRequest> getAllPageable(Long requesterId, int from, int size) {
        if (size == 0) {
            return Collections.emptyList();
        }
        userExist(requesterId);
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByCreated);
        List<ItemRequest> itemRequests = itemRequestRepository.findAll(page).stream()
                .filter(itemRequest -> !itemRequest.getId().equals(requesterId))
                .collect(Collectors.toList());

        List<Long> requestsIds = itemRequests.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<ItemInRequestProjection> answersOnRequests = itemRepository.findAllByRequestIdIn(requestsIds);

        itemRequests.forEach(itemRequest -> setItemsInRequest(itemRequest, answersOnRequests));

        return itemRequests;
    }

    private void setItemsInRequest(ItemRequest itemRequest, List<ItemInRequestProjection> answers) {
        List<ItemInRequestProjection> answerOnRequest = answers.stream()
                .filter(item -> itemRequest.getId().equals(item.getRequestId()))
                .collect(Collectors.toList());
        itemRequest.setItems(answerOnRequest);

    }

    private void userExist(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Запрос от несуществующего пользователся");
        }
    }


}
