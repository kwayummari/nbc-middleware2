package com.itrust.middlewares.nbc.auth.repositories;

import com.itrust.middlewares.nbc.auth.entities.AuthDataEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthDataEntity, Long>, JpaSpecificationExecutor<AuthDataEntity> {


    AuthDataEntity findTopByUsername(String username);

}
