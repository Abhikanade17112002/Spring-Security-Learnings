package com.springsecurity.repository;

import com.springsecurity.entity.Authorities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authorities,String> {
    Optional<Authorities> findByAuthorityName(String authorityName);
}


