package com.itrust.middlewares.nbc.logging.repository;

import com.itrust.middlewares.nbc.logging.entities.LoggingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoggingRepository extends JpaRepository<LoggingEntity, UUID>, JpaSpecificationExecutor<LoggingEntity> {

    @Query("select rqt from logging rqt where rqt.identifier = ?1 and rqt.category = ?2 and rqt.type = ?3 and rqt.timestamp < ?4 order by rqt.id desc limit 1")
    LoggingEntity findLatestSameRequest(String identifier, String category, String type, long timestamp);

}
