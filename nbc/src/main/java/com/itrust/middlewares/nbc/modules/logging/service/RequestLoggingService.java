package com.itrust.middlewares.nbc.modules.logging.service;

import com.itrust.middlewares.nbc.modules.logging.model.RequestLogging;
import com.itrust.middlewares.nbc.modules.logging.repository.RequestLoggingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestLoggingService {
    private final RequestLoggingRepository repository;

    @Async
    public void save(RequestLogging log) {
        repository.save(log);
    }
} 