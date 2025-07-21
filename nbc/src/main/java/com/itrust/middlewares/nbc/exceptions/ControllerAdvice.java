package com.itrust.middlewares.nbc.exceptions;

import com.itrust.middlewares.nbc.DateTimeUtil;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class ControllerAdvice {

    static Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    MessageSource messageSource;
    public ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<GenericRestResponse<?>> handleGenericException(GenericException ex, Locale locale) {
        logger.error("Generic exception: {}", ex.getMessage(), ex);
        return ResponseEntity.ok(createErrorResponse(ex.getErrorCode(), ex.getMessage(), ex.getMessages(), ex.getDetails()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(NoHandlerFoundException ex, Locale locale) {
        String message = String.format(ex.getMessage());
        HashMap<String, Object> messages = new HashMap<>();
        return new RestResponse(LocalDateTime.now().toString(), false,"000",message,messages);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public RestResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = String.format("Found %s validation error(s) ",ex.getBindingResult().getFieldErrors().size());
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new RestResponse(LocalDateTime.now().toString(), false,"602",message,errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(ConstraintViolationException ex, Locale locale) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .reduce(messageSource.getMessage("errors.found", null, locale), String::concat);
        Map<String, Object> errors = new HashMap<>();
        errors.put("message",ex.getMessage());
        return new RestResponse(LocalDateTime.now().toString(), false,"000",message,errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(EntityNotFoundException ex, Locale locale) {
        return new RestResponse(LocalDateTime.now().toString(), false,"000",ex.getMessage(),null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(MethodArgumentTypeMismatchException ex, Locale locale) {
        return new RestResponse(LocalDateTime.now().toString(), false,"000",ex.getMessage(),null);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(HttpClientErrorException.Unauthorized ex, Locale locale) {
        String message = String.format(ex.getMessage());
        return new RestResponse(LocalDateTime.now().toString(), false,"000",message,null);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(HttpClientErrorException ex, Locale locale) {
        String message = String.format(ex.getMessage());
        return new RestResponse(LocalDateTime.now().toString(), false,"000",message,null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    RestResponse handleException(Exception ex, Locale locale) {
        logger.error("Error processing questionnaire KYC request: {}", ex.getMessage());
        String message = String.format(ex.getMessage());
        return new RestResponse(LocalDateTime.now().toString(), false,"000",message,null,null);
    }

    private <T> GenericRestResponse<T> createErrorResponse(String code, String message, Map<String, Object> messages, T details) {
        return new GenericRestResponse<>(DateTimeUtil.dateTime(), false, code, message, messages, details);
    }


}
