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
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TIPSFSPRequestDTO implements Serializable {

    @NotBlank
    private String agentAccount;

    @NotBlank
    private String merchantId;

    @NotBlank
    private String terminalId;

    @NotBlank
    private String deviceId;

    @NotBlank
    private String partnerId;

    @NotBlank
    private String channel;

    @NotBlank
    private String signature;

}
