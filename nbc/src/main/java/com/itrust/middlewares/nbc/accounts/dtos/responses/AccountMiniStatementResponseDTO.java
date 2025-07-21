package com.itrust.middlewares.nbc.accounts.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link AccountMiniStatementResponseDTO}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountMiniStatementResponseDTO implements Serializable {
    private String statusCode;
    private String message;
    private String error;
    private String channelRef;
    private String actionCode;
    private Object data;
    private Object body;
    private Object summary;
    private Object transactions;

    @Override
    public String toString() {
        return "{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", actionCode='" + actionCode + '\'' +
                ", channelRef='" + channelRef + '\'' +
                ", data=" + data +
                ", body=" + body +
                ", summary=" + summary +
                ", transactions=" + transactions +
                '}';
    }
}
