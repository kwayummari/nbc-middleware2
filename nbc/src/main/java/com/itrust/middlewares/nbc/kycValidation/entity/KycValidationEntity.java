package com.itrust.middlewares.nbc.kycValidation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "kyc_validations")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KycValidationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(unique = true, length = 20)
    private String nin;

    private String validationType;

    private boolean verified;

    @Column(unique = true, length = 20)
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String rqCode;

    private String customer_number;

    private String account_number;

    private String kyc_ref_no;

    private String fingerCode;

    @Column(columnDefinition = "TEXT")
    private String fingerImage;

    @Column(length = 6, unique = true)
    private String otp;

    private Long otpExpiresAt;

    private String copReference;

    private String statusCode;

    private String message;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "kyc_validations_data_id", referencedColumnName = "id")
    private KycValidationDataEntity data;

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
