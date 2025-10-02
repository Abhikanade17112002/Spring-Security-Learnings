package com.springsecurity.repository;

import com.springsecurity.entity.Insurance;
import com.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    @Repository
    interface UserRepository extends JpaRepository<User,String> {
       Optional<UserDetails> findByUserName(String username);
    }
}