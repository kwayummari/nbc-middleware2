package com.itrust.middlewares.nbc.accounts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenCallbackRequestSuccessDataDTO implements Serializable {
    private String accountStatus;
    private String newAccountId;
    private String authorizedDebit;
    private String availableBalance;
    private String balanceOnHold;
    private String confirmationAmount;
    private String creditInterestAmount;
    private String currentBookBalance;
    private String customerShortName;
    private String debitInterestAmount;
    private String minimumBalance;
    private String netBalance;
    private String periodicAverageBalance;
    private String previousEODBookBalance;
    private String primaryTaxWithheld;
    private String secondaryTaxWithheld;
    private String serviceCharge;
    private String sweepinAmountOnLien;
    private String unClearFund;
    private String serviceChargeApplied;
    private String memo;
    private String userReferenceNumber;
    private String transactionDateTime;
    private String productCode;
    private String copreference;
}
