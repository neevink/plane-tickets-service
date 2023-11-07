package com.soa.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.soa.controller.EventController;
import com.soa.controller.TicketController;
import com.soa.dto.ErrorMessage;
import com.soa.error.ApplicationException;
import com.soa.exception.EntityNotFoundException;
import com.soa.exception.NotValidParamsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = {TicketController.class, EventController.class})
public class ProcessExceptionHandler {
    private final ErrorMessage errorMessage = new ErrorMessage();

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        System.out.println(e.getClass());
        return ResponseEntity.status(400)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleException(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(400)
                .body(errorMessage.setErrors(List.of("В GET-параметре передано слишком длинное число")));
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<?> handleException(ApplicationException e) {
        return ResponseEntity.status(400)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException e) {
        return ResponseEntity.status(400)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({NotValidParamsException.class})
    public ResponseEntity<?> handleNotValidParamsException(NotValidParamsException e) {
        return ResponseEntity.status(422)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(422)
                .body(errorMessage.setErrors(List.of("Произошла ошибка: " + e.getMessage())));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(422)
                .body(errorMessage.setErrors(List.of("Невалидные формат json:" + e.getMessage().split(";")[0])));
    }


    @ExceptionHandler({ConstraintViolationException.class, BindException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleValidationException(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            message = "Некорректные параметры " + getValidationError((MethodArgumentNotValidException) ex);
        }
        return ResponseEntity.badRequest()
                .body(errorMessage.setErrors(List.of(message)));
    }

    private String getValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(";"));
    }
}
