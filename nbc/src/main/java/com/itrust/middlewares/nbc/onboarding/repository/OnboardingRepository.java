package com.itrust.middlewares.nbc.onboarding.repository;

import com.itrust.middlewares.nbc.onboarding.entities.AccountEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;


@Repository("AccountOnboardingRepository")
public interface OnboardingRepository extends JpaRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {

    @Query(value = "SELECT * FROM accounts a WHERE a.nin = :nin LIMIT 1", nativeQuery = true)
    AccountEntity findFirstByNin(@Param("nin") String nin);

    @Query(value = "SELECT * FROM accounts a WHERE a.nin = :nin LIMIT 1", nativeQuery = true)
    Optional<AccountEntity> findByNin(@Param("nin") String nin);

    @Query(value = "SELECT * FROM accounts a WHERE a.nin = :nin and a.cop_reference = :copReference LIMIT 1", nativeQuery = true)
    Optional<AccountEntity> findFirstByNinAAndCopReference(@Param("nin") String nin, @Param("copReference") String copReference);

    @Query(value = "SELECT * FROM accounts a WHERE a.cop_reference = :reference ORDER BY a.id LIMIT 1", nativeQuery = true)
    AccountEntity findFirstByCopReference(@Param("reference") String reference);

    @Query("SELECT a FROM AccountEntity a WHERE a.copReference = :reference")
    AccountEntity findByCopReference(@Param("reference") String reference);

    @Transactional
    @Modifying
    @Query(value = "UPDATE accounts SET status = :status, response_code = :responseCode, response_message = :responseMessage WHERE nin = :nin", nativeQuery = true)
    void updateAfterNBC(@Param("nin") String nin,
                        @Param("status") String status,
                        @Param("responseCode") String responseCode,
                        @Param("responseMessage") String responseMessage);

    @Transactional
    @Modifying
    @Query(value = "UPDATE accounts SET retries = :retries WHERE nin = :nin", nativeQuery = true)
    void updateRetry(@Param("nin") String nin,
                              @Param("retries") int retry);

    @Transactional
    @Modifying
    @Query(value = "UPDATE accounts SET status = :status WHERE nin = :nin", nativeQuery = true)
    void updateStatus(@Param("nin") String nin,
                              @Param("status") String status);

}
