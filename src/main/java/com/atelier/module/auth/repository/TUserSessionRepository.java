package com.atelier.module.auth.repository;

import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.auth.model.entity.TUserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TUserSessionRepository extends JpaRepository<TUserSession, Long> {
    @Query("SELECT s FROM TUserSession s WHERE s.user = :user AND s.isActive = true")
    Optional<TUserSession> findByUserAndIsActive(@Param("user") MUser user);

}

