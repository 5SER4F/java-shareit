package ru.practicum.shareit.exception.exceptions;

public class IllegalEmailException extends RuntimeException {

    public IllegalEmailException() {
        super();
    }

    public IllegalEmailException(String message) {
        super(message);
    }
}
