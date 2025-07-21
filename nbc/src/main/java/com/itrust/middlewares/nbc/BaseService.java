package com.itrust.middlewares.nbc;

import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.responses.GenericRestResponse;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class BaseService {

    @Value("${notification_url}")
    private String notificationUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected RestResponse errorResponse(String message, String messageCode, @Nullable Map<String, Object> messages, @Nullable Object data){
        return new RestResponse(LocalDateTime.now().toString(), false,messageCode,message,messages,data);
    }

    protected RestResponse exceptionResponse(Exception exception, @Nullable String statusCode){
        if(statusCode == null){
            statusCode = "000";
        }
        logger.error(exception.getMessage(), exception);
        logSlack("An exception occurred and Error has been logged: " + exception.getMessage());
        Map<String, Object> messages = new HashMap<>();
        return new RestResponse(DateTimeUtil.dateTime(), false,statusCode,"Internal Server Error",messages);
    }

    protected RestResponse successResponse(String message, String successCode, @Nullable Map<String, Object> messages, @Nullable Object data){
        return new RestResponse(DateTimeUtil.dateTime(), true,successCode,message,messages,data);
    }

    protected <T> GenericRestResponse <T> errorResponseGeneric(String message, String messageCode, @Nullable Map<String, Object> messages, @Nullable T data){
        return new GenericRestResponse<T>(LocalDateTime.now().toString(), false,messageCode,message,messages,data);
    }

    protected <T> GenericRestResponse <T> exceptionResponseGeneric(Exception exception, @Nullable String statusCode){
        if(statusCode == null){
            statusCode = "000";
        }
        logger.error(exception.getMessage(), exception);
        logSlack("An exception occurred and Error has been logged: " + exception.getMessage());
        Map<String, Object> messages = new HashMap<>();
        return new GenericRestResponse<T>(DateTimeUtil.dateTime(), false,statusCode,"Internal Server Error",messages,null);
    }

    protected <T> GenericRestResponse<T> successResponseGeneric(String message, String successCode, @Nullable Map<String, Object> messages, @Nullable T data){
        return new GenericRestResponse<T>(DateTimeUtil.dateTime(), true,successCode,message,messages,data);
    }

    protected void logInfo(String message){
        if(logger.isInfoEnabled()){
            logger.info(message);
        }
    }

    protected void logWarn(String message){
        if(logger.isWarnEnabled()){
            logger.warn(message);
        }
    }

    protected void logError(String message){
        if(logger.isErrorEnabled()){
            logger.error(message);
        }
    }

    protected void logDebug(String message){
        if(logger.isDebugEnabled()){
            logger.debug(message);
        }
    }

    @Async
    protected void logSlack(String message){
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> request = slackFactory(message, restTemplate, notificationUrl);
        restTemplate.postForObject("/sendSlackMessage", request, RestResponse.class);
    }

    Map<String, String> slackFactory(String message, RestTemplate restTemplate, String notificationUrl) {
        return getStringStringMap(message, restTemplate, notificationUrl);
    }

    @NotNull
    static Map<String, String> getStringStringMap(String message, RestTemplate restTemplate, String notificationUrl) {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(notificationUrl));
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("content-type", MediaType.APPLICATION_JSON_VALUE);
            request.getHeaders().add("accept", MediaType.APPLICATION_JSON_VALUE);
            return execution.execute(request, body);
        });
        Map<String, String> request = new HashMap<>();
        request.put("message", message);
        request.put("channel", "nbc-middleware");
        return request;
    }

}
