package com.itrust.middlewares.nbc.modules.dpworld.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DPOlInquiryResponseDto implements Serializable {

    private int statusCode;
    private String message;
    private String channelRef;
    private String spCode;
    private String billRef;
    private Payer payer;
    private BillDetails billDetails;
    private Double totalCharges; // Nullable

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payer {
        private String fullName;
        private String language;
        private String email;
        private String branch;
        private String phone;
        private String account;

        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (Exception e) {
                return "{}";

            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BillDetails {
        private String billRef;
        private String serviceName;
        private String description;
        private String billCreatedAt;
        private String totalAmount;
        private String balance;
        private String phoneNumber;
        private String email;
        private String billedName;
        private String currency;
        private String paymentMode;
        private String expiryDate;
        private String creditAccount;
        private String creditCurrency;
        private ExtraFields extraFields;

        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (Exception e) {
                return "{}";

            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExtraFields implements Serializable {
        private String merchantCode;
        private String invoiceNumber;

        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
            } catch (Exception e) {
                return "{}";

            }
        }
    }
}
