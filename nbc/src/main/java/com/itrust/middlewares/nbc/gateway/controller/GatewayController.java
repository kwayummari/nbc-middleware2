package com.itrust.middlewares.nbc.gateway.controller;


import com.itrust.middlewares.nbc.BaseController;
import com.itrust.middlewares.nbc.exceptions.ResponseCode;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import org.apache.camel.ProducerTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("services")
public class GatewayController extends BaseController {

    private final ProducerTemplate producerTemplate;

    public GatewayController(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    @GetMapping("/brokerage")
    public RestResponse accountOptions() {
       try {
           // Call Camel route using ProducerTemplate
           String result = producerTemplate.requestBody("direct:validateRequest",null, String.class);
           return successResponse(ResponseCode.SUCCESS.getMessage(), ResponseCode.SUCCESS.getCode(), null, result);
       }catch (Exception e) {
           return exceptionResponse(e,null);
       }
    }

}