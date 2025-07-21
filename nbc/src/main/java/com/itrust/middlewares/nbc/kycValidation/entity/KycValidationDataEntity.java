package com.itrust.middlewares.nbc.kycValidation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "kyc_validations_data")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KycValidationDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    private String surName;

    private String residentPostalAddress;

    private String nin;

    private String residentDistrict;

    private String birthCountry;

    private String residentPostCode;

    private String residentWard;

    private String email;

    private String placeOfBirth;

    private String residentRegion;

    private String sex;

    private String birthDistrict;

    private String dateOfBirth;

    private String residentVillage;

    private String residentHouseNo;

    private String firstName;

    private String birthRegion;

    private String birthWard;

    private String otherNames;

    private String phoneNumber;

    private String nationality;

    private String registeredFingerPrints;

    private String middleName;

    private String maritalStatus;
    
    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String photo;
    
    @Column(columnDefinition = "TEXT")
    private String signature;

    @OneToOne(mappedBy = "data")
    private KycValidationEntity data;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
