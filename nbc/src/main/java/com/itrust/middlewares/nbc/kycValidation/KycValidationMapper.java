package com.itrust.middlewares.nbc.kycValidation;

import com.itrust.middlewares.nbc.kycValidation.dtos.NidaResponseFullDTO;
import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationDataEntity;
import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationEntity;

public class KycValidationMapper {

    public static KycValidationEntity mapToKycValidationEntity(NidaResponseFullDTO nidaResponse) {
        if (nidaResponse == null) {
            return null;
        }

        KycValidationDataEntity dataEntity = KycValidationDataEntity.builder()
                .surName(nidaResponse.getSurName())
                .residentPostalAddress(nidaResponse.getResidentPostalAddress())
                .nin(nidaResponse.getNin())
                .residentDistrict(nidaResponse.getResidentDistrict())
                .birthCountry(nidaResponse.getBirthCountry())
                .residentPostCode(nidaResponse.getResidentPostCode())
                .residentWard(nidaResponse.getResidentWard())
                .email(nidaResponse.getEmail())
                .placeOfBirth(nidaResponse.getPlaceOfBirth())
                .residentRegion(nidaResponse.getResidentRegion())
                .sex(nidaResponse.getSex())
                .birthDistrict(nidaResponse.getBirthDistrict())
                .dateOfBirth(nidaResponse.getDateOfBirth())
                .residentVillage(nidaResponse.getResidentVillage())
                .residentHouseNo(nidaResponse.getResidentHouseNo())
                .firstName(nidaResponse.getFirstName())
                .birthRegion(nidaResponse.getBirthRegion())
                .birthWard(nidaResponse.getBirthWard())
                .otherNames(nidaResponse.getOtherNames())
                .phoneNumber(nidaResponse.getPhoneNumber())
                .nationality(nidaResponse.getNationality())
                .registeredFingerPrints(nidaResponse.getRegisteredFingerPrints())
                .middleName(nidaResponse.getMiddleName())
                .maritalStatus(nidaResponse.getMaritalStatus())
                .status(nidaResponse.getStatus())
                .photo(nidaResponse.getPhoto())
                .signature(nidaResponse.getSignature())
                .build();

        return KycValidationEntity.builder()
                .nin(nidaResponse.getNin())
                .phoneNumber(nidaResponse.getPhoneNumber())
                .email(nidaResponse.getEmail())
                .rqCode(nidaResponse.getRqCode())
                .copReference(nidaResponse.getCopReference())
                .statusCode(nidaResponse.getStatus())
                .message("Mapped from NidaResponseFullDTO")
                .data(dataEntity)
                .build();
    }
}