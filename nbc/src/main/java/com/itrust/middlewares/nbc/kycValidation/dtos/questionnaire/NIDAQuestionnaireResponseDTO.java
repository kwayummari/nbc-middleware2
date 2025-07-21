package com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NIDAQuestionnaireResponseDTO {
    private int statusCode;
    private String message;
    private NIDAQuestionnaireResponseDataDTO data;
    private String error;
}
