package com.itrust.middlewares.nbc.gepg.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Data Transfer Object for GePG payment details.
 * 
 * This class represents the detailed information about a payment in the
 * Government Electronic Payment Gateway (GePG) system. It contains all the specific
 * transaction details, payer information, and payment amounts.
 * 
 * @author iTrust
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class PmtDtls implements Serializable {

    /** The channel transaction ID */
    @JsonProperty("ChannelTrxId")
    public String channelTrxId;

    /** The service provider code */
    @JsonProperty("SpCode")
    public String spCode;

    /** The payment reference ID */
    @JsonProperty("PayRefId")
    public String payRefId;

    /** The bill control number */
    @JsonProperty("BillCtrNum")
    public String billCtrNum;

    /** The amount paid */
    @JsonProperty("PaidAmt")
    public String paidAmt;

    /** The transaction date and time */
    @JsonProperty("TrxDtTm")
    public String trxDtTm;

    /** The payment option */
    @JsonProperty("PayOpt")
    public String payOpt;

    /** The payment plan */
    @JsonProperty("PayPlan")
    public String payPlan;

    /** The bill amount */
    @JsonProperty("BillAmt")
    public String billAmt;

    /** The minimum payment amount */
    @JsonProperty("MinPayAmt")
    public String minPayAmt;

    /** The currency code */
    @JsonProperty("Ccy")
    public String ccy;

    /** The third-party transaction ID */
    @JsonProperty("TrdPtyTrxId")
    public String trdPtyTrxId;

    /** The payer's cell phone number */
    @JsonProperty("PyrCellNum")
    public String pyrCellNum;

    /** The payer's name */
    @JsonProperty("PyrName")
    public String pyrName;

    /** The payer's email address */
    @JsonProperty("PyrEmail")
    public String pyrEmail;

    /** The payer's ID */
    @JsonProperty("PyrId")
    public String pyrId;

    /** The type of ID provided by the payer */
    @JsonProperty("PyrIdType")
    public String pyrIdType;

    /** The amount debited from the payer's account */
    @JsonProperty("DebitAmount")
    public String debitAmount;

    /** The deal ID */
    @JsonProperty("DealID")
    public String dealID;

    /** Reserved field 1 for future use */
    @JsonProperty("Rsv1")
    public String rsv1;

    /** Reserved field 2 for future use */
    @JsonProperty("Rsv2")
    public String rsv2;

    /** Reserved field 3 for future use */
    @JsonProperty("Rsv3")
    public String rsv3;


}
