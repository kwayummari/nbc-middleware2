package com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.itrust.middlewares.nbc.validations.nida.types.NinValidation;
import com.itrust.middlewares.nbc.validations.nida.types.IdTypeValidation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NIDAQuestionnaireRequestDTO implements Serializable {

    @NotNull(message = "The field NIN can not be empty")
    @Size(min = 20, max = 20, message = "The field NIN must be 20 characters")
    private String nin;

    @NotNull(message = "The field IDTYPE can not be empty")
    @Size(min = 6, max = 6, message = "The field IdType must be 6 characters")
    private String idType;

    private String rqCode;

    private String qnAnsw;

    @Override
    public String toString() {
        return "{" +
                "nin='" + nin + '\'' +
                ", idType='" + idType + '\'' +
                ", rqCode='" + rqCode + '\'' +
                ", qnAnsw='" + qnAnsw + '\'' +
                '}';
    }
}
