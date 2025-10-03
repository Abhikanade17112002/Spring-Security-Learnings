package com.springsecurity.repository;

import com.springsecurity.entity.User;
import com.springsecurity.type.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<UserDetails> findByUserName(String username);

    Optional<User> findByProviderIdAndProviderTypeOrEmailId(String providerId, ProviderType authProviderType , String userEmail);

}
