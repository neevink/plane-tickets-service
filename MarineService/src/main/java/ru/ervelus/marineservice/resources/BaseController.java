package ru.ervelus.marineservice.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.egormit.library.ErrorResponse;

import javax.persistence.EntityNotFoundException;

public abstract class BaseController {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.ok(ErrorResponse.of(404, "Item not found"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestParams(IllegalArgumentException e) {
        return ResponseEntity.ok(ErrorResponse.of(400, "Validation failed"));
    }
}
