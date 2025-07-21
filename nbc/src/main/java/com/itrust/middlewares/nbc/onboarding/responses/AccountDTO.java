package com.itrust.middlewares.nbc.onboarding.responses;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountDTO {

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String nin;

    String status;

    String passport;

    String passportExpireDate;

    String firstName;

    String middleName;

    String lastName;

    String sex;

    String othernames;

    String shortname;

    String countryofResidence;

    String currentPhonenumber;

    String residentPostaladdress;

    String birthcountry;

    String email;

    String title;

    String monthlyIncome;

    String educationLevel;

    String spouseName;

    String spousePhone;

    String flgPep;

    String language;

    String tinNumber;

    String nationality;

    String region;

    String district;

    String ward;

    String placeBirth;

    String bankAccountNumber;

    String bankAccountName;

    String bankBranch;

    String bankName;

    String employmentStatus;

    String otherBusiness;

    String businessSector;

    String employerName;

    String otherEmployment;

    String currentOccupation;

    String sourceOfIncome;

    String incomeFrequency;

    String kinName;

    String kinMobile;

    String kinEmail;

    String kinRelationship;

    String otp;

    String copReference;

    String accountId;

    String accountStatus;

    String arthaReference;

    String dseCds;

    String dseCdsStatus;

    String cif;

    String brokerageStatus;

    String innovaClientCode;

    String innovaClientCodeStatus;

    String cbsCif;

    String cbsCifStatus;

    String cbsKyc;

    String cbsKycStatus;

    String cbsAcc;

    String cbsAccStatus;

    String retries;

    String responseCode;

    String responseMessage;

    @Override
    public String toString() {
        return "{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", nin='" + nin + '\'' +
                ", status='" + status + '\'' +
                ", passport='" + passport + '\'' +
                ", passportExpireDate='" + passportExpireDate + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex='" + sex + '\'' +
                ", othernames='" + othernames + '\'' +
                ", shortname='" + shortname + '\'' +
                ", countryofResidence='" + countryofResidence + '\'' +
                ", currentPhonenumber='" + currentPhonenumber + '\'' +
                ", residentPostaladdress='" + residentPostaladdress + '\'' +
                ", birthcountry='" + birthcountry + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", monthlyIncome='" + monthlyIncome + '\'' +
                ", educationLevel='" + educationLevel + '\'' +
                ", spouseName='" + spouseName + '\'' +
                ", spousePhone='" + spousePhone + '\'' +
                ", flgPep='" + flgPep + '\'' +
                ", language='" + language + '\'' +
                ", tinNumber='" + tinNumber + '\'' +
                ", nationality='" + nationality + '\'' +
                ", region='" + region + '\'' +
                ", district='" + district + '\'' +
                ", ward='" + ward + '\'' +
                ", placeBirth='" + placeBirth + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", bankName='" + bankName + '\'' +
                ", employmentStatus='" + employmentStatus + '\'' +
                ", otherBusiness='" + otherBusiness + '\'' +
                ", businessSector='" + businessSector + '\'' +
                ", employerName='" + employerName + '\'' +
                ", otherEmployment='" + otherEmployment + '\'' +
                ", currentOccupation='" + currentOccupation + '\'' +
                ", sourceOfIncome='" + sourceOfIncome + '\'' +
                ", incomeFrequency='" + incomeFrequency + '\'' +
                ", kinName='" + kinName + '\'' +
                ", kinMobile='" + kinMobile + '\'' +
                ", kinEmail='" + kinEmail + '\'' +
                ", kinRelationship='" + kinRelationship + '\'' +
                ", otp='" + otp + '\'' +
                ", copReference='" + copReference + '\'' +
                ", accountId='" + accountId + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", arthaReference='" + arthaReference + '\'' +
                ", dseCds='" + dseCds + '\'' +
                ", dseCdsStatus='" + dseCdsStatus + '\'' +
                ", cif='" + cif + '\'' +
                ", brokerageStatus='" + brokerageStatus + '\'' +
                ", innovaClientCode='" + innovaClientCode + '\'' +
                ", innovaClientCodeStatus='" + innovaClientCodeStatus + '\'' +
                ", cbsCif='" + cbsCif + '\'' +
                ", cbsCifStatus='" + cbsCifStatus + '\'' +
                ", cbsKyc='" + cbsKyc + '\'' +
                ", cbsKycStatus='" + cbsKycStatus + '\'' +
                ", cbsAcc='" + cbsAcc + '\'' +
                ", cbsAccStatus='" + cbsAccStatus + '\'' +
                ", retries='" + retries + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
