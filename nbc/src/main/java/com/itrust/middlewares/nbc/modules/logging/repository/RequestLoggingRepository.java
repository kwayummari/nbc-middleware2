package com.itrust.middlewares.nbc.modules.logging.repository;

import com.itrust.middlewares.nbc.modules.logging.model.RequestLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLoggingRepository extends JpaRepository<RequestLogging, Integer> {
} 