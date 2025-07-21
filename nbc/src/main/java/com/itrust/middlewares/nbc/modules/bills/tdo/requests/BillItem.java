package com.itrust.middlewares.nbc.modules.bills.tdo.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillItem implements Serializable {

    @NotBlank
    private String spCode;

    @NotBlank
    private String shortName;

    @NotBlank
    private String fullName;

    @NotBlank
    private Boolean active;

    @NotBlank
    private String category;


    @Override
    public String toString() {
        return "{" +
                "spCode='" + spCode + '\'' +
                ", shortName='" + shortName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", active=" + active +
                ", category='" + category + '\'' +
                '}';
    }
}
