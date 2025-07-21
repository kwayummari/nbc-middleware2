package com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class NIDAQuestionnaireResponseDataDTO {

    @JsonProperty("status")
    private String status;

    @JsonProperty("nin")
    private String nin;

    @JsonProperty("en")
    private String en;

    @JsonProperty("rqCode")
    private String rqcode;

    @JsonProperty("sw")
    private String sw;

    @JsonProperty("prevAnsCode")
    private String prevanscode;

    @JsonProperty("answer")
    private String answer;

    @JsonProperty("copReference")
    private String copReference;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("middleName")
    private String middlename;

    @JsonProperty("surName")
    private String surname;

    @JsonProperty("otherNames")
    private String othernames;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("dob")
    private String dob;

    @JsonProperty("residentRegion")
    private String residentregion;

    @JsonProperty("residentDistrict")
    private String residentdistrict;

    @JsonProperty("residentWard")
    private String residentward;

    @JsonProperty("residentVillage")
    private String residentvillage;

    @JsonProperty("residentStreet")
    private String residentstreet;

    @JsonProperty("residentHouseNo")
    private String residenthouseno;

    @JsonProperty("residentPostalAddress")
    private String residentpostaladdress;

    @JsonProperty("residentPostCode")
    private String residentpostcode;

    @JsonProperty("birthCountry")
    private String birthcountry;

    @JsonProperty("birthRegion")
    private String birthregion;

    @JsonProperty("birthDistrict")
    private String birthdistrict;

    @JsonProperty("birthWard")
    private String birthward;

    @JsonProperty("birthCertificateNo")
    private String birthcertificateno;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("phoneNumber")
    private String phonenumber;

    @JsonProperty("maritalStatus")
    private String maritalstatus;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("signature")
    private String signature;


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
