package com.itrust.middlewares.nbc.modules.dpworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class DPOInquiryRequestDto implements Serializable {

    private String spCode;
    private String customerAccount;
    private String billRef;

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
