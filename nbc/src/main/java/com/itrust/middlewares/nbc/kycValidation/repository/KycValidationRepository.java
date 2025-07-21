package com.itrust.middlewares.nbc.kycValidation.repository;

import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KycValidationRepository extends JpaRepository<KycValidationEntity, UUID>, JpaSpecificationExecutor<KycValidationEntity> {

    KycValidationEntity findFirstByNin(@Param("nin") String kycId);

    @Modifying
    @Query("delete from kyc_validations k where k.nin=:nin")
    void deleteValidation(@Param("nin") String nin);

    @Modifying
    @Query(value = "UPDATE kyc_validations k SET otp = :otp, cop_reference = :cop, phone_number = :phone,  email = :email WHERE k.nin = :nin", nativeQuery = true)
    void updateOtp(@Param("nin") String nin, @Param("otp") String otp, @Param("cop") String cop, @Param("email") String email, @Param("phone") String phone);


    @Modifying
    @Transactional
    @Query("UPDATE kyc_validations k SET k.verified = true WHERE k.nin = :nin")
    int verifyOtp(@Param("nin") String nin);

    @Modifying
    @Transactional
    @Query("UPDATE kyc_validations k SET k.copReference = :cop WHERE k.nin = :nin")
    int updateCOP(@Param("nin") String nin, @Param("cop") String cop);

}
