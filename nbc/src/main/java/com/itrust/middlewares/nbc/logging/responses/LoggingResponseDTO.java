package com.itrust.middlewares.nbc.logging.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class LoggingResponseDTO {

    public String id;

    public String identifier;

    public String category;

    public String type;

    public String time;

    public LocalDateTime timestamp;

    public String hostname;

    public String method;

    public String controllerAction;

    public String middleware;

    public String path;

    public String status;

    public String duration;

    public String ipAddress;

    public String memoryUsage;

    public String tags;

    public String stackTrace;

    public String causer;

    public String authenticatedUser;

    public String payload;

    public String header;

    public String response;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", identifier='" + identifier + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", time='" + time + '\'' +
                ", timestamp=" + timestamp +
                ", hostname='" + hostname + '\'' +
                ", method='" + method + '\'' +
                ", controllerAction='" + controllerAction + '\'' +
                ", middleware='" + middleware + '\'' +
                ", path='" + path + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", memoryUsage='" + memoryUsage + '\'' +
                ", tags='" + tags + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", causer='" + causer + '\'' +
                ", authenticatedUser='" + authenticatedUser + '\'' +
                ", payload='" + payload + '\'' +
                ", header='" + header + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
