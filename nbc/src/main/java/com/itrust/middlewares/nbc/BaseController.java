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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BaseController{

    @Value("${notification_url}")
    private String notificationUrl;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    protected RestResponse errorResponse(String message, String messageCode, Map<String, Object> messages, @Nullable Object data){
        return new RestResponse(LocalDateTime.now().toString(), false,messageCode,message,messages, data);
    }

    protected RestResponse successResponse(String message, String successCode, Map<String, Object> messages, @Nullable Object data){
        return new RestResponse(DateTimeUtil.dateTime(), true,successCode,message,messages, data);
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

    protected GenericRestResponse<?> exceptionResponseGeneric(Exception exception, @Nullable String statusCode){
        if(statusCode == null){
            statusCode = "000";
        }
        logger.error(exception.getMessage(), exception);
        logSlack("An exception occurred and Error has been logged: " + exception.getMessage());
        Map<String, Object> messages = new HashMap<>();
        return new GenericRestResponse<>(DateTimeUtil.dateTime(), false,statusCode,"Internal Server Error",messages,null);
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

    @Async
    protected void logSlack(String message){
        RestTemplate restTemplate = new RestTemplate();
        Map<String,String> request = slackFactory(message, restTemplate, notificationUrl);
        restTemplate.postForObject("/sendSlackMessage", request, RestResponse.class);
    }

    Map<String, String> slackFactory(String message, RestTemplate restTemplate, String notificationUrl) {
        return getStringStringMap(message, restTemplate, notificationUrl);
    }

    @NotNull
    static Map<String, String> getStringStringMap(String message, RestTemplate restTemplate, String notificationUrl) {
        return BaseService.getStringStringMap(message, restTemplate, notificationUrl);
    }

}
