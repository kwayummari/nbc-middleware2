package com.itrust.middlewares.nbc.accounts.dtos.callback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.Accessors;
import java.io.Serializable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenCallbackRequestDataDTO implements Serializable {

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
    private String customerId;
    private String copreference;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            
            return "{}"; // Return empty JSON if an error occurs
        }
    }
}
