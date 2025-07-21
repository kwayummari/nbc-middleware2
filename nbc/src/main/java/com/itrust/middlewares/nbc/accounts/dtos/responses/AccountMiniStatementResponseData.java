package com.itrust.middlewares.nbc.accounts.dtos.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link AccountMiniStatementResponseData}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountMiniStatementResponseData implements Serializable {
    private String accountId;
    private String accountTitle;
    private String productName;
    private String amountCharge;
    private String closingBalance;
    private String crcount;
    private String currencyName;
    private String drCount;
    private String noOfTransactions;
    private String openingBalance;
    private String totalCreditAmount;
    private String totalDebitAmount;

    @Override
    public String toString() {
        return "{" +
                "accountId='" + accountId + '\'' +
                ", accountTitle='" + accountTitle + '\'' +
                ", productName='" + productName + '\'' +
                ", amountCharge='" + amountCharge + '\'' +
                ", closingBalance='" + closingBalance + '\'' +
                ", crcount='" + crcount + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", drCount='" + drCount + '\'' +
                ", noOfTransactions='" + noOfTransactions + '\'' +
                ", openingBalance='" + openingBalance + '\'' +
                ", totalCreditAmount='" + totalCreditAmount + '\'' +
                ", totalDebitAmount='" + totalDebitAmount + '\'' +
                '}';
    }
}
