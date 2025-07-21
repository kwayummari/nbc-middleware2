package com.itrust.middlewares.nbc.kycValidation.dtos.questionnaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CorrectedNIDAData {

    private String status;
    private String nin;
    private String rqCode;
    private String en;
    private String sw;
    private String prevAnsCode;
    private String answer;
    private String copreference;
    private String firstname;
    private String middlename;
    private String surname;
    private String othernames;
    private String sex;
    private String dob;
    private String residentregion;
    private String residentdistrict;
    private String residentward;
    private String residentvillage;
    private String residentstreet;
    private String residenthouseno;
    private String residentpostaladdress;
    private String residentpostcode;
    private String birthcountry;
    private String birthregion;
    private String birthdistrict;
    private String birthward;
    private String birthcertificateno;
    private String nationality;
    private String phonenumber;
    private String maritalstatus;
    private String photo;
    private String signature;

    public NIDAQuestionnaireResponseDataDTO toActualDTO() {
        NIDAQuestionnaireResponseDataDTO dto = new NIDAQuestionnaireResponseDataDTO();
        dto.setStatus(status);
        dto.setNin(nin);
        dto.setRqcode(rqCode);
        dto.setEn(en);
        dto.setSw(sw);
        dto.setPrevanscode(prevAnsCode);
        dto.setAnswer(answer);
        dto.setCopReference(copreference);
        dto.setFirstname(firstname);
        dto.setMiddlename(middlename);
        dto.setSurname(surname);
        dto.setOthernames(othernames);
        dto.setSex(sex);
        dto.setDob(dob);
        dto.setResidentregion(residentregion);
        dto.setResidentdistrict(residentdistrict);
        dto.setResidentward(residentward);
        dto.setResidentvillage(residentvillage);
        dto.setResidentstreet(residentstreet);
        dto.setResidenthouseno(residenthouseno);
        dto.setResidentpostaladdress(residentpostaladdress);
        dto.setResidentpostcode(residentpostcode);
        dto.setBirthcountry(birthcountry);
        dto.setBirthregion(birthregion);
        dto.setBirthdistrict(birthdistrict);
        dto.setBirthward(birthward);
        dto.setBirthcertificateno(birthcertificateno);
        dto.setNationality(nationality);
        dto.setPhonenumber(phonenumber);
        dto.setMaritalstatus(maritalstatus);
        dto.setPhoto(photo);
        dto.setSignature(signature);
        return dto;
    }
}
