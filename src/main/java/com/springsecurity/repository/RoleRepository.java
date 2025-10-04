package com.springsecurity.repository;

import com.springsecurity.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles,String> {
    Optional<Roles> findByRoleName(String roleName);
}
