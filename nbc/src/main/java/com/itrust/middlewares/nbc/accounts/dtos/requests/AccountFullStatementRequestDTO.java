package com.itrust.middlewares.nbc.accounts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountFullStatementRequestDTO {

    @NonNull
    private String accountNumber;

    @NonNull
    private String startDate;

    @NonNull
    private String endDate;

    private String email;

    @Override
    public String toString() {
        return "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", payer=" + email +
                '}';
    }
}
