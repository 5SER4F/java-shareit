package ru.practicum.shareit.exception.exceptions;

public class SelfBookingException extends RuntimeException {
    public SelfBookingException() {
    }

    public SelfBookingException(String message) {
        super(message);
    }
}
