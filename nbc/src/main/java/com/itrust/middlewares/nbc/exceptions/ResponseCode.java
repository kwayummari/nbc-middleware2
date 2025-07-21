package com.itrust.middlewares.nbc.exceptions;

import lombok.Getter;

@Getter
public enum ResponseCode {

    INTERNAL_SERVER_ERROR("000","Internal Server Error"),
    SUCCESS("600","Success"),
    DUPLICATE_TRANSACTION("601","Duplicate Transaction"),
    VALIDATION_FAILED("602","Validation failed"),
    TRANSACTION_FAILED("603","Transaction failed to save"),
    NO_RESPONSE_FROM_THIRD_PARTY("604","No response from third party"),
    ACCOUNT_NOT_FOUND("605","Account not found"),
    INPUT_ERROR("606","Input error"),
    TRANSACTION_FAILED_AT_CORE_BANKING("607","Transaction failed at core banking"),
    NO_DATA_FOUND_ON_DB("608","No data found on DB"),
    AUTHENTICATION_FAILED("609","Authentication failed"),
    CLIENT_NOT_REGISTERED("610","Client not registered"),
    INVALID_CREDENTIALS("611","Invalid credentials"),
    REQUESTED_SERVICE_IS_UNAVAILABLE("612","Service is unavailable"),
    REQUESTED_SERVICE_IS_UNAUTHORIZED("613","Service is unauthorized"),
    FAILED_TO_SAVE_ON_DB("614","Failed to save on db"),
    BILLER_DOES_NOT_EXIST("615","Biller does not exist"),
    THIRD_PARTY_NOT_FOUND("616","Third party not found"),
    LDAP_AUTH_FAILED("617","LDAP auth failed"),
    HTTP_STATUS_ERROR("618","HTTP status error"),
    GEPG_INQ_VALIDATION_FAILED("619","GEPG inq validation failed"),
    AMOUNT_VALIDATION_FAILED("620","Amount validation failed"),
    REQUEST_FAILED_AT_BILLER("621","Request failed at biller"),
    NOT_ALLOWED_TO_PERFORM_BILLS_REQUEST("622","Not allowed to perform bills request"),
    EOD_HAS_BEEN_COMPLETED("623","EOD has been completed"),
    EOD_NOT_DONE_FOR_THE_DAY("624","EOD is not done for this day"),
    NO_RESPONSE_FROM_FCC("625","No response from FCC"),
    TRANSACTION_FAILED_AT_FCC("626","Transaction failed at fcc"),
    OUTGOING_FT_SUCCESS("627","Outgoing FT Success"),
    PRE_PAID_BILL_FAILED_AT_GEPG("628","Pre-paid bill failed at gpg"),
    FAILURE_TO_SAVE_CUSTOMER_SIGNATURE("629","Failure to save customer signature"),
    CURRENCY_ACCOUNT_COMBINATION_DOES_NOT_MATCH("630","Currency account combination does not match"),
    BILLER_NOT_DEFINED_FOR_BOT_TRANSFER("631","Biller not defined for bot transfer"),
    TRANSACTION_NOT_ALLOWED_AT_THIS_TIME("632","Transaction not allowed at this time, Try later"),
    ENTRY_COUNT_MISMATCH_FROM_GEPG("633","Entry count mismatch from gpg"),
    NO_ACT_FROM_BILLER("634","No act from biller"),
    TRANSACTION_NEEDS_AUTHORIZATION("635","Transaction needs authorization"),
    ALL_ENTRIES_ARE_OK_RE_PUSH_NOT_ALLOWED("636","All entries are ok re push not allowed"),


    THIRD_PARTY_ERROR("700","Third party error"),
    ACCOUNT_ALREADY_EXISTS("701","Account already exists"),
    EMPTY_RESPONSE_FROM_THIRD_PARTY("702","Empty response from third-party");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
