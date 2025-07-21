package com.itrust.middlewares.nbc.kycValidation.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NidaResponseFullDTO {

    @JsonProperty("surName")
    public String surName;

    @JsonProperty("signature")
    public String signature;

    @JsonProperty("residentPostalAddress")
    public String residentPostalAddress;

    @JsonProperty("nin")
    public String nin;

    @JsonProperty("residentDistrict")
    public String residentDistrict;

    @JsonProperty("birthCountry")
    public String birthCountry;

    @JsonProperty("residentPostCode")
    public String residentPostCode;

    @JsonProperty("previousAnswecode")
    public String previousAnswecode;

    @JsonProperty("copReference")
    private String copReference;

    @JsonProperty("residentWard")
    public String residentWard;

    @JsonProperty("email")
    public String email;

    @JsonProperty("placeOfBirth")
    public String placeOfBirth;

    @JsonProperty("sw")
    public String sw;

    @JsonProperty("residentRegion")
    public String residentRegion;

    @JsonProperty("sex")
    public String sex;

    @JsonProperty("birthDistrict")
    public String birthDistrict;

    @JsonProperty("photo")
    public String photo;

    @JsonProperty("en")
    public String en;

    @JsonProperty("dateOfBirth")
    public String dateOfBirth;

    @JsonProperty("residentVillage")
    public String residentVillage;

    @JsonProperty("rqCode")
    public String rqCode;

    @JsonProperty("residentHouseNo")
    public String residentHouseNo;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("birthRegion")
    public String birthRegion;

    @JsonProperty("birthWard")
    public String birthWard;

    @JsonProperty("otherNames")
    public String otherNames;

    @JsonProperty("phoneNumber")
    public String phoneNumber;

    @JsonProperty("nationality")
    public String nationality;

    @JsonProperty("registeredFingerPrints")
    public String registeredFingerPrints;

    @JsonProperty("middleName")
    public String middleName;

    @JsonProperty("maritalStatus")
    public String maritalStatus;

    @JsonProperty("status")
    public String status;

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

