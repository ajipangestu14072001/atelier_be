package com.atelier.module.user.repository;

import com.atelier.module.user.model.entity.MRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MRoleRepository extends JpaRepository<MRole, Long> {
    Optional<MRole> findByRole(String role);
}