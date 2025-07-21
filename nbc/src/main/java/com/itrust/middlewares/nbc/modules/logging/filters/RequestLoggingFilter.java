package com.itrust.middlewares.nbc.modules.logging.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.modules.logging.model.RequestLogging;
import com.itrust.middlewares.nbc.modules.logging.service.RequestLoggingService;
import io.opentelemetry.api.trace.Span;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final RequestLoggingService logService;
    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    @Autowired
    public RequestLoggingFilter(RequestLoggingService logService) {
        this.logService = logService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
            throws ServletException, IOException {


        long startTime = System.currentTimeMillis();

        // Wrap request/response to read body
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(response);

        // Extract headers, handling multivalued headers
        Map<String, Object> headersMap = new HashMap<>();
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            headersMap.put(headerName, headerValues.size() == 1 ? headerValues.get(0): headerValues);
        });

        Map<String, Object> authHeaders = new HashMap<>();
        Collections.list(request.getHeaderNames()).stream()
                .filter(h -> h.toLowerCase().startsWith("auth"))
                .forEach(h -> {
                    List<String> headerValues = Collections.list(request.getHeaders(h));
                    authHeaders.put(h, headerValues.size() == 1 ? headerValues.get(0) : headerValues);
                });

        Map<String, Object> cookieHeaders = new HashMap<>();
        Collections.list(request.getHeaderNames()).stream()
                .filter(h -> h.equalsIgnoreCase("cookie"))
                .forEach(h -> {
                    List<String> headerValues = Collections.list(request.getHeaders(h));
                    cookieHeaders.put(h, headerValues.size() == 1 ? headerValues.get(0) : headerValues);
                });

        String requestBody = StreamUtils.copyToString(wrappedRequest.getInputStream(), StandardCharsets.UTF_8);

        // Get trace ID
        Span span = Span.current();
        String traceId = span.getSpanContext().getTraceId();

        Instant timestamp = Instant.now();

        // Extract user ID from security context if available
        Map<String, Object> userCif = extractUserIdAndCif(request);
        String userId = userCif.get("userId") != null ? userCif.get("userId").toString() : null;
        String customerNumber = userCif.get("cif") != null ? userCif.get("cif").toString() : null;

        // Extract metadata
        String clientIp = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String sessionId = request.getSession(false) != null ? (request).getSession().getId() : null;
        String deviceId = (request).getHeader("X-Device-Id"); // Adjust based on your device ID header
        String referrer = (request).getHeader("Referer");

        // Geolocation (placeholder)
        Map<String, Object> geoLocation = new HashMap<>();
        geoLocation.put("country", "unknown");
        geoLocation.put("city", "unknown");

        // Determine if bodies are JSON
        Boolean isRequestBodyJson = isJson(requestBody);
        boolean isResponseBodyJson;

        // Extract business context
        String service = extractService(request);
        String product = extractProduct(request);
        String action = extractAction(request);

        try {
            chain.doFilter(wrappedRequest, wrappedResponse);
        } catch (Exception e) {
            // Log the exception details before rethrowing
            logException(e, wrappedRequest, wrappedResponse, startTime, traceId, timestamp,
                    headersMap, authHeaders, cookieHeaders, requestBody,
                    userId, customerNumber, clientIp, userAgent, sessionId, deviceId, referrer,
                    geoLocation, isRequestBodyJson, service, product, action);
            throw e;
        }

        // Capture response details
        String responseBody = wrappedResponse.getContent();
        Map<String, Object> responseHeaders = new HashMap<>();
        wrappedResponse.getHeaderNames().forEach(headerName -> {
            List<String> headerValues =  wrappedResponse.getHeaders(headerName).stream().toList();
            responseHeaders.put(headerName, headerValues.size() == 1 ? headerValues.get(0) : headerValues);
        });
        isResponseBodyJson = isJson(responseBody);
        int status = wrappedResponse.getStatus();

        // Calculate processing time
        int processingTimeMs = (int) (System.currentTimeMillis() - startTime);

        // Build log entry
        RequestLogging log = new RequestLogging();
        log.setMethod(request.getMethod());
        log.setPath(request.getRequestURI());
        log.setRequestId(traceId);
        log.setAuthHeader(authHeaders);
        log.setCookieHeader(cookieHeaders);
        log.setRequestHeaders(sanitizeHeaders(headersMap));
        log.setResponseHeaders(sanitizeHeaders(responseHeaders));
        log.setRequestBody(sanitizeBody(truncate(requestBody)));
        log.setResponseBody(sanitizeBody(truncate(responseBody)));
        log.setIsRequestBodyJson(isRequestBodyJson);
        log.setIsResponseBodyJson(isResponseBodyJson);
        log.setResponseStatus(status);
        log.setTimestamp(timestamp);
        log.setService(service);
        log.setProduct(product);
        log.setAction(action);
        log.setCustomerNumber(customerNumber);
        log.setClientIp(clientIp);
        log.setUserAgent(userAgent);
        log.setUserId(userId);
        log.setSessionId(sessionId);
        log.setDeviceId(deviceId);
        log.setReferrer(referrer);
        log.setGeoLocation(geoLocation);
        log.setProcessingTimeMs(processingTimeMs);

        // Save log
        logService.save(log);
    }

    private void logException(Exception e, HttpServletRequest req, CachedBodyHttpServletResponse wrappedResponse,
                              long startTime, String traceId, Instant timestamp,
                              Map<String, Object> headersMap, Map<String, Object> authHeaders,
                              Map<String, Object> cookieHeaders, String requestBody,
                              String userId, String customerNumber, String clientIp,
                              String userAgent, String sessionId, String deviceId,
                              String referrer, Map<String, Object> geoLocation,
                              Boolean isRequestBodyJson, String service, String product, String action) {
        String responseBody = wrappedResponse.getContent();
        Map<String, Object> responseHeaders = new HashMap<>();
        wrappedResponse.getHeaderNames().forEach(headerName -> {
            List<String> headerValues = wrappedResponse.getHeaders(headerName).stream().toList();
            responseHeaders.put(headerName, headerValues.size() == 1 ? headerValues.get(0) : headerValues);
        });
        Boolean isResponseBodyJson = isJson(responseBody);
        int status = wrappedResponse.getStatus() != 0 ? wrappedResponse.getStatus() : 500; // Default to 500 for exceptions
        int processingTimeMs = (int) (System.currentTimeMillis() - startTime);

        RequestLogging log = new RequestLogging();
        log.setErrorMessage(e.getClass().getSimpleName() + ": " + e.getMessage());
        log.setMethod(req.getMethod());
        log.setPath(req.getRequestURI());
        log.setRequestId(traceId);
        log.setAuthHeader(authHeaders);
        log.setCookieHeader(cookieHeaders);
        log.setRequestHeaders(headersMap);
        log.setResponseHeaders(responseHeaders);
        log.setRequestBody(truncate(requestBody));
        log.setResponseBody(truncate(responseBody));
        log.setIsRequestBodyJson(isRequestBodyJson);
        log.setIsResponseBodyJson(isResponseBodyJson);
        log.setResponseStatus(status);
        log.setTimestamp(timestamp);
        log.setService(service);
        log.setProduct(product);
        log.setAction(action);
        log.setCustomerNumber(customerNumber);
        log.setClientIp(clientIp);
        log.setUserAgent(userAgent);
        log.setUserId(userId);
        log.setSessionId(sessionId);
        log.setDeviceId(deviceId);
        log.setReferrer(referrer);
        log.setGeoLocation(geoLocation);
        log.setProcessingTimeMs(processingTimeMs);

        logService.save(log);
    }

    private Map<String, Object> sanitizeHeaders(Map<String, Object> headers) {
        Map<String, Object> sanitized = new HashMap<>(headers);
        if (sanitized.containsKey("Authorization")) {
            sanitized.put("Authorization", "[REDACTED]");
        }
        return sanitized;
    }

    private String sanitizeBody(String body) {
        return body.replaceAll("\"password\":\"[^\"]+\"", "\"password\":\"[REDACTED]\"");
    }

    private static String truncate(String value) {
        if (value == null) return null;
        return value.length() <= 1048576 ? value : value.substring(0, 1048576);
    }

    private boolean isJson(String value) {
        try {
            new ObjectMapper().readTree(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractService(HttpServletRequest request) {
        return request.getHeader("X-Service-Name");
    }

    private String extractProduct(HttpServletRequest request) {
        return request.getHeader("X-Product-Name");
    }

    private String extractAction(HttpServletRequest request) {
        return request.getHeader("X-Action");
    }

    private Map<String,Object> extractUserIdAndCif(HttpServletRequest request) {
        Map<String, Object> UserAndCif = new HashMap<>();
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            try {
                DecodedJWT jwt = JWT.decode(token.substring(7));
                UserAndCif.put("cif",jwt.getClaim("cif").asString());
                UserAndCif.put("userId",jwt.getClaim("userId").asString());
                return UserAndCif;
            } catch (Exception ignored) {}
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                String jwtCookieName = "access_token";
                if (jwtCookieName.equals(cookie.getName())) {
                    try{
                        token =  cookie.getValue();
                        DecodedJWT jwt = JWT.decode(token);
                        UserAndCif.put("cif",jwt.getClaim("cif").asString());
                        UserAndCif.put("userId",jwt.getClaim("userId").asString());
                        return UserAndCif;
                    } catch (Exception e) {
                        return  null; // If decoding fails, return null
                    }
                }
            }
        }

        logger.info("No CIF obtained from JWT, returning null");
        UserAndCif.put("cif",null);
        UserAndCif.put("userId",null);

        return UserAndCif;
    }
}