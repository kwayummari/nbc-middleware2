package com.itrust.middlewares.nbc.fundtransfer.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link FundTransferResponseDTO}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTransferResponseDTO implements Serializable {
    private String statusCode;
    private String message;
    private String error;
    private String actionCode;
    private String spCode;
    private String requestType;
    private String channelRef;
    private String timestamp;
    private String gatewayRef;
}
