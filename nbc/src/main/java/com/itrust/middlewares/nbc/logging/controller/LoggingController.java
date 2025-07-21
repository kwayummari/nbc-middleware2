package com.itrust.middlewares.nbc.logging.controller;


import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.services.LoggingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logging")
public class LoggingController extends BaseController {

    private final LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @GetMapping("/")
    public RestResponse index(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try{
            return  loggingService.getLogs(page, size);
        }catch(Exception e){
            return exceptionResponse(e,null);
        }
    }
}
