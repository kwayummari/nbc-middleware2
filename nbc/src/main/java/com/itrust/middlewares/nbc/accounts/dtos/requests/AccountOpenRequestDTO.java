package com.itrust.middlewares.nbc.accounts.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountOpenRequestDTO {


    private String callBackUrl;

    private String nin;

    private String branchCode;

    private String othernames;

    private String shortname;

    private String countryofResidence;

    private String currentPhonenumber;

    private String residentPostaladdress;

    private String birthcountry;

    private String email;

    private String title;

    private String occupation;

    private String designation;

    private String jobposition;

    private String employer;

    private String employerIndustry;

    private String employerAddress;

    private String monthlyIncome;

    private String educationLevel;

    private List<String> sourceofFund;

    private String spouseName;

    private String spousePhone;

    private boolean otpVerified;

    private List<String> productcode;

    private String internationalTrans;

    private List<String> transactcountries;

    private String flgPep;

    private String language;

    private String salesCode;

    private String tinNumber;

    private String nationality;

    private String customerInvitedThrough;

    private String passport;

    private String passportExpireDate;

    private String dseAccount;

    private String region;

    private String district;

    private String ward;

    private String placeBirth;

    private String bankAccountNumber;

    private String bankAccountName;

    private String bankBranch;

    private String bankName;

    private String employmentStatus;

    private String employerName;

    private String otherEmployment;

    private String currentOccupation;

    private String sourceOfIncome;

    private String incomeFrequency;

    private String kinName;

    private String kinMobile;

    private String kinEmail;

    private String kinRelationship;

    private String otherBusiness;

    private String businessSector;

    private String otp;

    private String copReference;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }catch (Exception e){
            return "{}";
        }
    }
}
