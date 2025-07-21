package com.itrust.middlewares.nbc.onboarding.requests;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAccountDTO implements Serializable {

    @NotNull(message = "NIN cannot be null")
    @NotBlank(message = "NIN cannot be Blank")
    @Size(min = 20, max = 20, message = "Invalid NIN")
    String nin;

    String passport;
    String passportExpireDate;

    @NotNull(message = "The field firstName cannot be NULL")
    @NotBlank(message = "The field firstName cannot be Blank")
    @Size( max = 255, message = "The field FIRSTNAME cannot exceed 255 CHARACTERS")
    String firstName;

    @NotNull(message = "The field middleName cannot be NULL")
    @NotBlank(message = "The field middleName cannot be Blank")
    @Size( max = 255, message = "The field MIDDLENAME cannot exceed 255 CHARACTERS")
    String middleName;

    @NotNull(message = "The field Surname cannot be NULL")
    @NotBlank(message = "The field Surname cannot be Blank")
    @Size( max = 255, message = "The field Surname cannot exceed 255 CHARACTERS")
    String surName;

    @NotNull(message = "The field othernames cannot be NULL")
    @NotBlank(message = "The field othernames cannot be Blank")
    @Size( max = 1531, message = "The field OTHERNAMES cannot exceed 1531 CHARACTERS")
    String otherNames;

    @NotNull(message = "The field sex cannot be NULL")
    @NotBlank(message = "The field sex cannot be Blank")
    @Size( max = 6, message = "Invalid Sex")
    String sex;

    @NotNull(message = "The field shortname cannot be NULL")
    @NotBlank(message = "The field shortname cannot be Blank")
    @Size( max = 1531, message = "The field shortname cannot exceed 1531 CHARACTERS")
    String shortName;

    @NotNull(message = "The field countryofResidence cannot be NULL")
    @NotBlank(message = "The field countryofResidence cannot be Blank")
    @Size( max = 2,min = 2, message = "Invalid Country of Residence")
    String countryOfResidence;

    @NotNull(message = "The field currentPhonenumber cannot be NULL")
    @NotBlank(message = "The field currentPhonenumber cannot be Blank")
    @Size(min = 12, max = 12, message = "Invalid Mobile number")
    String currentPhoneNumber;

    @NotNull(message = "The field residentPostaladdress cannot be NULL")
    @NotBlank(message = "The field residentPostaladdress cannot be Blank")
    @Size( max = 1531, message = "The field residentPostaladdress cannot exceed 1531 CHARACTERS")
    String residentPostalAddress;

    @NotNull(message = "The field birthcountry cannot be NULL")
    @NotBlank(message = "The field birthcountry cannot be Blank")
    @Size( max = 2,min = 2, message = "Invalid Country of Birth")
    String birthCountry;

    @NotNull(message = "The field email cannot be NULL")
    @NotBlank(message = "The field email cannot be Blank")
    @Email(message = "Invalid email")
    String email;

    @NotNull(message = "The field title cannot be NULL")
    @NotBlank(message = "The field title cannot be Blank")
    @Size( max = 10, message = "The field title cannot  CHARACTERS")
    String title;

    @NotNull(message = "The field monthlyIncome cannot be NULL")
    @NotBlank(message = "The field monthlyIncome cannot be Blank")
    String monthlyIncome;

    String educationLevel;
    String spouseName;
    String spousePhone;
    String productCode;

    @NotNull(message = "The field sourceOfFund cannot be NULL")
    @NotBlank(message = "The field sourceOfFund cannot be Blank")
    String sourceOfFund;

//    @NotNull(message = "The field designation cannot be NULL")
//    @NotBlank(message = "The field designation cannot be Blank")
    String designation;

    @NotNull(message = "The field flagPeg cannot be NULL")
    @NotBlank(message = "The field flagPeg cannot be Blank")
    String flgPep;

    @NotNull(message = "The field language cannot be NULL")
    @NotBlank(message = "The field language cannot be Blank")
    String language;

//    @NotNull(message = "The field tinNumber cannot be NULL")
//    @NotBlank(message = "The field tinNumber cannot be Blank")
//    @Size( max = 9,min = 9, message = "Invalid TIN Number")
    String tinNumber;

    @NotNull(message = "The field nationality cannot be NULL")
    @NotBlank(message = "The field nationality cannot be Blank")
    @Size( max = 2,min = 2, message ="Invalid Nationality")
    String nationality;

    String dseAccount;

    @NotNull(message = "The field region cannot be NULL")
    @NotBlank(message = "The field region cannot be Blank")
    String region;

    @NotNull(message = "The field district cannot be NULL")
    @NotBlank(message = "The field district cannot be Blank")
    String district;

    @NotNull(message = "The field ward cannot be NULL")
    @NotBlank(message = "The field ward cannot be Blank")
    String ward;

    @NotNull(message = "The field placeBirth cannot be NULL")
    @NotBlank(message = "The field placeBirth cannot be Blank")
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

    @NotNull(message = "The field sourceOfIncome cannot be NULL")
    @NotBlank(message = "The field sourceOfIncome cannot be Blank")
    String sourceOfIncome;

    @NotNull(message = "The field incomeFrequency cannot be NUll")
    @NotBlank(message = "The field incomeFrequency cannot be Blank")
    String incomeFrequency;

    @NotNull(message = "The field kinName cannot be NULL")
    @NotBlank(message = "The field kinName cannot be Blank")
    String kinName;

    @NotNull(message = "The field kinMobile cannot be NULL")
    @NotBlank(message = "The field kinMobile cannot be Blank")
    String kinMobile;

    @NotNull(message = "The field kinEmail cannot be NULL")
    @NotBlank(message = "The field kinEmail cannot be Blank")
    @Email()
    String kinEmail;

    @NotNull(message = "The field kinRelationship  cannot be NULL")
    @NotBlank(message = "The field kinRelationship  cannot be Blank")
    String kinRelationship;

    @NotNull(message = "The field copReference  cannot be NULL")
    @NotBlank(message = "The field copReference  cannot be Blank")
    String copReference;

    String otp;

    @NotNull
    @NotBlank
    String artthaReference;

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
