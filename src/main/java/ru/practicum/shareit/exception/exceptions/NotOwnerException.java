package ru.practicum.shareit.exception.exceptions;

public class NotOwnerException extends RuntimeException {
    public NotOwnerException() {
        super();
    }

    public NotOwnerException(String message) {
        super(message);
    }
}
