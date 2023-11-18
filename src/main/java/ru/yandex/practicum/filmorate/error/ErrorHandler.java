package ru.yandex.practicum.filmorate.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerEntityNotFoundException(final NotFoundException exception) {
        log.info("Данные не найдены {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerHttpMediaTypeNotAcceptableException(final HttpMediaTypeNotAcceptableException exception) {
        log.info("Данные не найдены {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerValidationException(final ValidationException exception) {
        log.info("Ошибка валидации {}", exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

}