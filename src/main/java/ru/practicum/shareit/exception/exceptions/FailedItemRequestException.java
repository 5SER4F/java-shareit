package ru.practicum.shareit.exception.exceptions;

public class FailedItemRequestException extends RuntimeException {
    public FailedItemRequestException() {
        super();
    }

    public FailedItemRequestException(String message) {
        super(message);
    }
}
