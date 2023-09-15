package se.ifmo.ru.firstservice.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import se.ifmo.ru.firstservice.exception.NotFoundException;
import se.ifmo.ru.firstservice.util.ResponseUtils;
import se.ifmo.ru.firstservice.web.model.Error;

import javax.persistence.NoResultException;

@ControllerAdvice
public class MainControllerAdvice {
    private final ResponseUtils responseUtils;

    @Autowired
    public MainControllerAdvice(ResponseUtils responseUtils) {
        this.responseUtils = responseUtils;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Error> validationException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Error> validationParamException(MethodArgumentTypeMismatchException e){
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Invalid param supplied");
    }


    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<Error> noResultException(NoResultException e){
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Error> validationException(HttpMessageNotReadableException e){
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.METHOD_NOT_ALLOWED, "Validation exception");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> methodNotFound(HttpRequestMethodNotSupportedException e){
        return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, "Not Found");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Error> handleNotFoundException(NotFoundException e) {
        return responseUtils.buildResponseWithMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException e) {
        return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Error> handleThrowable(Throwable e) {
        e.printStackTrace();
        return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
