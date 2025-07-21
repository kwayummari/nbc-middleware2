package com.itrust.middlewares.nbc.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itrust.middlewares.nbc.modules.bills.tdo.requests.BillItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NBCResponseDTO {

    private String statusCode;
    private String message;
    private String error;
    private String channelRef;
    private String amount;
    private String actionCode;
    private Object data;
    private Object body;
    private Object payer;
    private List<BillItem> billers;
    private List<Map<String,Object >> errors;

    private Map<String,String > messages;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            return "{}";
        }
    }
}
