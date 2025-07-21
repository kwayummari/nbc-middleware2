package com.itrust.middlewares.nbc.kycValidation.dtos.biometric;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NIDABiometricResponseDataDTO implements Serializable {

    @JsonProperty("status")
    private String status;

    @JsonProperty("copReference")
    private String copreference;

    @JsonProperty("nin")
    private String nin;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("middleName")
    private String middlename;

    @JsonProperty("answer")
    private String answer;

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

    @JsonProperty("residentPostalAddress")
    private String residentpostaladdress;

    @JsonProperty("residentHouseNo")
    private String residenthouseno;

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

    @JsonProperty("rqCode")
    private String rqcode;

    @Override
    public String toString() {
        return "{" +
                "status='" + status + '\'' +
                ", copreference='" + copreference + '\'' +
                ", nin='" + nin + '\'' +
                ", firstname='" + firstname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", answer='" + answer + '\'' +
                ", surname='" + surname + '\'' +
                ", othernames='" + othernames + '\'' +
                ", sex='" + sex + '\'' +
                ", dob='" + dob + '\'' +
                ", residentregion='" + residentregion + '\'' +
                ", residentdistrict='" + residentdistrict + '\'' +
                ", residentward='" + residentward + '\'' +
                ", residentvillage='" + residentvillage + '\'' +
                ", residentstreet='" + residentstreet + '\'' +
                ", residentpostaladdress='" + residentpostaladdress + '\'' +
                ", residenthouseno='" + residenthouseno + '\'' +
                ", residentpostcode='" + residentpostcode + '\'' +
                ", birthcountry='" + birthcountry + '\'' +
                ", birthregion='" + birthregion + '\'' +
                ", birthdistrict='" + birthdistrict + '\'' +
                ", birthward='" + birthward + '\'' +
                ", birthcertificateno='" + birthcertificateno + '\'' +
                ", nationality='" + nationality + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", maritalstatus='" + maritalstatus + '\'' +
                ", photo='" + photo + '\'' +
                ", signature='" + signature + '\'' +
                ", rqcode='" + rqcode + '\'' +
                '}';
    }
}
