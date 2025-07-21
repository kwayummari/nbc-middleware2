package com.itrust.middlewares.nbc.accounts.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * DTO for {@link AccountEnquiryResponseDTO}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountEnquiryResponseDTO  {
    private String statusCode;
    private String message;
    private String error;
    private String actionCode;
    private Object data;
}
