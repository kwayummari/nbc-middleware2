package com.itrust.middlewares.nbc.exceptions;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class GenericException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> messages;
    private final Object details;
    private final Throwable cause;

    public GenericException(String message, String errorCode, @Nullable Map<String, Object> messages, @Nullable Object details, @Nullable Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
        this.messages = messages;
        this.cause = cause;
    }

}