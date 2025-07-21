package com.itrust.middlewares.nbc.onboarding.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity()
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    String updatedAt;

    @Column(length = 20, unique = true, updatable = false,nullable = false)
    String nin;

    @Column(nullable = false)
    String status;

    String passport;

    String passportExpireDate;

//    @Column(nullable = false)
    String firstName;

    String middleName;

//    @Column(nullable = false)
    String lastName;

//    @Column(nullable = false)
    String sex;

//    @Column(nullable = false, length = 1531)
    String othernames;

//    @Column(nullable = false, length = 1531)
    String shortname;

//    @Column(nullable = false)
    String countryofResidence;

//    @Column(nullable = false)
    String currentPhonenumber;

//    @Column(nullable = false)
    String residentPostaladdress;

//    @Column(nullable = false)
    String birthcountry;

//    @Column(nullable = false)
    String email;

//    @Column(nullable = false)
    String title;

    String monthlyIncome;

    String educationLevel;

    String spouseName;

    String spousePhone;

//    @Column(nullable = false)
    String flgPep;

//    @Column(nullable = false)
    String language;

//    @Column(nullable = false)
    String tinNumber;

//    @Column(nullable = false)
    String nationality;

//    @Column(nullable = false)
    String region;

//    @Column(nullable = false)
    String district;

//    @Column(nullable = false)
    String ward;

//    @Column(nullable = false)
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

//    @Column(nullable = false)
    String sourceOfIncome;

//    @Column(nullable = false)
    String incomeFrequency;

//    @Column(nullable = false)
    String kinName;

//    @Column(nullable = false)
    String kinMobile;

//    @Column(nullable = false)
    String kinEmail;

//    @Column(nullable = false)
    String kinRelationship;

//    @Column(nullable = false)
    String otp;

    @Column(unique = true)
    String copReference;

    String accountId;

    String customerId;

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

//    @Column(nullable = false)
    int retries;

    String responseCode;

    String responseMessage;

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