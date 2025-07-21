package com.itrust.middlewares.nbc.logging.services;

import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class LoggingService extends BaseService {

    private final LoggingRepository repository;

    public LoggingService(LoggingRepository repository) {
        this.repository = repository;
    }

    public LoggingEntity saveRequestHistory(LoggingEntity loggingEntity) {
        return repository.save(loggingEntity);
    }

    public RestResponse getLogs(int page, int size) {
        try{
            Page<LoggingEntity> entities = repository.findAll(PageRequest.of(page, size));
            return successResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null, entities);
        }catch (Exception e){
            return exceptionResponse(e,null);
        }
    }

    public RestResponse getLogs() {
        try{
            Page<LoggingEntity> entities = repository.findAll(PageRequest.of(0, 10));
            return successResponse(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null, entities);
        }catch (Exception e){
            return exceptionResponse(e,null);
        }
    }
}
