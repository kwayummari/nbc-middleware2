package com.itrust.middlewares.nbc.kycValidation.repository;

import com.itrust.middlewares.nbc.kycValidation.entity.KycValidationDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@ResponseBody
public interface KycValidationDataRepository extends JpaRepository<KycValidationDataEntity, UUID>, JpaSpecificationExecutor<KycValidationDataEntity> {
    KycValidationDataEntity findFirstByNin(String nin);
}