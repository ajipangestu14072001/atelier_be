package com.atelier.module.auth.repository;

import com.atelier.module.user.model.entity.MUser;
import com.atelier.module.auth.model.entity.TUserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TUserAuthRepository extends JpaRepository<TUserAuth, Long> {
    Optional<TUserAuth> findByPassword(String password);
    Optional<TUserAuth> findByUser(MUser user);
}

