package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.*;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ResourceNotFoundException.class, NotOwnerException.class, SelfBookingException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFoundException(final RuntimeException e) {
        return Map.of("Запрошенный ресурс не существует", e.getMessage());
    }

    @ExceptionHandler({FailedBookingException.class, FailedCommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleFailedBookingException(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}
