package com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NIDAQuestionnaireWrapperResponse implements Serializable {

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Map<String, Object> data; // Temporarily hold raw data

    @JsonProperty("error")
    private Object error;

    public NIDAQuestionnaireResponseDataDTO getParsedData() {
        ObjectMapper mapper = new ObjectMapper();
        if(data == null || data.isEmpty()) {
            return new NIDAQuestionnaireResponseDataDTO();
        }
        return mapper.convertValue(data, CorrectedNIDAData.class).toActualDTO();
    }
}
