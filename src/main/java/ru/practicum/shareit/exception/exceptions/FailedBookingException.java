package ru.practicum.shareit.exception.exceptions;

public class FailedBookingException extends RuntimeException {
    public FailedBookingException() {
    }

    public FailedBookingException(String message) {
        super(message);
    }
}
