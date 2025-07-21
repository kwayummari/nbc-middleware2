package com.itrust.middlewares.nbc.accounts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for {@link AccountOpenNBCRequestDTO}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenNBCRequestDTO {

    @NotBlank() @NotNull
    private String callBackUrl;

    @NotBlank()
    private String nin;

    @NotBlank()
    private String branchCode;

    private String othernames;

    private String shortname;

    @NotBlank()
    private String countryofResidence;

    @NotBlank()
    private String currentPhonenumber;

    private String residentPostaladdress;

    @NotBlank()
    private String birthcountry;

    @NotBlank()
    private String email;

    @NotBlank()
    private String title;

    @NotBlank()
    private String occupation;

    //todo user will input for now put 'MANAGER'
    private String designation;

    //todo user will input can be blanck for now
    private String jobposition;

    //todo user will input can be blanck for now
    private String employer;

    //todo user will input can be blanck for now
    private String employerIndustry;

    //todo user will input can be blanck for now
    private String employerAddress;

    //todo User to input
    @NotBlank()
    private String monthlyIncome;

    @NotBlank()
    private String educationLevel;

    @NotBlank()
    private List<String> sourceofFund;

    private String spouseName;

    private String spousePhone;

    @NotBlank()
    private String otp;

    @NotBlank()
    private boolean otpVerified;

    @NotBlank()
    private List<String> productcode;

    @NotBlank()
    private String internationalTrans;

    @NotBlank()
    private List<String> transactcountries;

    @NotBlank()
    private String flgPep;

    @NotBlank()
    private String language;

    private String salesCode;

    private String tinNumber;

    @NotBlank()
    private String nationality;

    @NotBlank()
    private String customerInvitedThrough;

    private String copReference;

    @Override
    public String toString() {
        return "{" +
                "callBackUrl='" + callBackUrl + '\'' +
                ", nin='" + nin + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", othernames='" + othernames + '\'' +
                ", shortname='" + shortname + '\'' +
                ", countryofResidence='" + countryofResidence + '\'' +
                ", currentPhonenumber='" + currentPhonenumber + '\'' +
                ", residentPostaladdress='" + residentPostaladdress + '\'' +
                ", birthcountry='" + birthcountry + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", occupation='" + occupation + '\'' +
                ", designation='" + designation + '\'' +
                ", jobposition='" + jobposition + '\'' +
                ", employer='" + employer + '\'' +
                ", employerIndustry='" + employerIndustry + '\'' +
                ", employerAddress='" + employerAddress + '\'' +
                ", monthlyIncome='" + monthlyIncome + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", sourceofFund=" + sourceofFund +
                ", spouseName='" + spouseName + '\'' +
                ", spousePhone='" + spousePhone + '\'' +
                ", otp='" + otp + '\'' +
                ", otpVerified=" + otpVerified +
                ", productcode=" + productcode +
                ", internationalTrans='" + internationalTrans + '\'' +
                ", transactcountries=" + transactcountries +
                ", flgPep='" + flgPep + '\'' +
                ", language='" + language + '\'' +
                ", salesCode='" + salesCode + '\'' +
                ", tinNumber='" + tinNumber + '\'' +
                ", nationality='" + nationality + '\'' +
                ", customerInvitedThrough='" + customerInvitedThrough + '\'' +
                ", copReference='" + copReference + '\'' +
                '}';
    }
}
