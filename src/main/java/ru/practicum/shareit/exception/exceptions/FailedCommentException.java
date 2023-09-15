package ru.practicum.shareit.exception.exceptions;

public class FailedCommentException extends RuntimeException {
    public FailedCommentException() {
    }

    public FailedCommentException(String message) {
        super(message);
    }
}
