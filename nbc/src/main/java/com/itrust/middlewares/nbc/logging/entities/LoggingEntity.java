package com.itrust.middlewares.nbc.logging.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity(name = "logging")
@NoArgsConstructor
@Setter
@Getter
public class LoggingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String identifier;

    private String category;

    private String type;

    private String time;

    @Column(nullable = false)
    private long timestamp;

    private String hostname;

    private String method;

    private String controllerAction;

    private String middleware;

    private String path;

    private int status;

    private int duration;

    private int ipAddress;

    private int memoryUsage;

    private int tags;

    @Column(columnDefinition = "TEXT")
    private String stackTrace;

    @Column(columnDefinition = "TEXT")
    private String causer;

    @Column(columnDefinition = "TEXT")
    private String authenticatedUser;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(columnDefinition = "TEXT")
    private String header;

    @Column(columnDefinition = "TEXT", length = 10000)
    private String response;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
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
                ", status=" + status +
                ", duration=" + duration +
                ", ipAddress=" + ipAddress +
                ", memoryUsage=" + memoryUsage +
                ", tags=" + tags +
                ", stackTrace='" + stackTrace + '\'' +
                ", causer='" + causer + '\'' +
                ", authenticatedUser='" + authenticatedUser + '\'' +
                ", payload='" + payload + '\'' +
                ", header='" + header + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
