package com.itrust.middlewares.nbc.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.logging.log4j.message.ObjectArrayMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {

    @Setter
    private String timestamp;
    @Setter
    private Boolean status;
    @Setter
    private String statusCode;
    @Setter
    private String message;

    private Map<String, Object> messages;

    private Object data;

    public RestResponse(String timestamp, Boolean status, String statusCode, String message, Map<String, Object> messages) {
        this.timestamp = timestamp;
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.messages = messages;
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
