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
public class BillsConfirmRequestDTO implements Serializable {

    @NotBlank
    private String channelRef;

    @NotBlank
    private String amount;

    @Override
    public String toString() {
        return "{" +
                "channelRef='" + channelRef + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

}
