package com.itrust.middlewares.nbc.accounts.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link AccountOpenResponseDTO}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenResponseDTO implements Serializable {
    private String statusCode;
    private String message;
    private String error;
    private String actionCode;
    private Object data;
}
