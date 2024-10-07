package com.atelier.module.user.repository;

import com.atelier.module.user.model.entity.MUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MUserRepository extends JpaRepository<MUser, Long> {
    Optional<MUser> findByEmailOrPhone(String email, String phone);
    Optional<MUser> findByInternalId(String internalId);
    Optional<MUser> findByUsername(String username);
    Optional<MUser> findByEmail(String email);
}

