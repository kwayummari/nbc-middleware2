package com.itrust.middlewares.nbc.modules.logging.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "request_logging")
public class RequestLogging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('request_logging_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 16)
    @Column(name = "method", length = 16)
    private String method;

    @Size(max = 1024)
    @Column(name = "path", length = 1024)
    private String path;

    @Size(max = 64)
    @Column(name = "request_id", length = 64)
    private String requestId;

    @Column(name = "auth_header")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> authHeader;

    @Column(name = "cookie_header")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> cookieHeader;

    @Column(name = "request_headers")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> requestHeaders;

    @Column(name = "response_headers")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> responseHeaders;

    @Column(name = "request_body", length = Integer.MAX_VALUE)
    private String requestBody;

    @Column(name = "response_body", length = Integer.MAX_VALUE)
    private String responseBody;

    @Column(name = "is_request_body_json")
    private Boolean isRequestBodyJson;

    @Column(name = "is_response_body_json")
    private Boolean isResponseBodyJson;

    @Column(name = "response_status")
    private Integer responseStatus;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp")
    private Instant timestamp;

    // Enriched business context
    @Size(max = 128)
    @Column(name = "service", length = 128)
    private String service;

    @Size(max = 128)
    @Column(name = "product", length = 128)
    private String product;

    @Size(max = 128)
    @Column(name = "action", length = 128)
    private String action;

    @Size(max = 64)
    @Column(name = "customer_number", length = 64)
    private String customerNumber;

    // Additional metadata
    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent")
    private String userAgent;

    @Size(max = 64)
    @Column(name = "user_id", length = 64)
    private String userId;

    @Size(max = 128)
    @Column(name = "session_id", length = 128)
    private String sessionId;

    @Size(max = 128)
    @Column(name = "device_id", length = 128)
    private String deviceId;

    @Column(name = "referrer")
    private String referrer;

    @Column(name = "geo_location")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> geoLocation;

    @Column(name = "processing_time_ms")
    private Integer processingTimeMs;

    @Column(name = "success")
    @Generated(GenerationTime.ALWAYS)
    private Boolean success;

    @Column(name = "error_message")
    private String errorMessage;
}