package com.springsecurity.entity;

import com.springsecurity.type.ProviderType;
import com.springsecurity.type.RoleType;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String emailId;

    @Column(nullable = false)
    private String password;

    @ManyToMany( fetch = FetchType.EAGER )
    @JoinTable(
            name = "UserRolesMapping" ,
            joinColumns = @JoinColumn( name = "userId") ,
            inverseJoinColumns = @JoinColumn( name = "roleId")
    )
    private List<Roles> roles = new ArrayList<>() ;

    private String providerId ;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType ;

    public User() {
    }

    public User(String userId, String firstName, String lastName, String userName, String emailId, String password, List<Roles> roles, String providerId, ProviderType providerType) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
        this.roles = roles;
        this.providerId = providerId;
        this.providerType = providerType;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public String getEmailId() {
        return emailId;
    }

    @Override
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Roles role : roles) {
            // Add the role itself
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));

            // Add the authorities linked to this role
            for (Authorities authority : role.getAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
            }
        }
        return grantedAuthorities;
    }

    // ✅ Return true instead of super
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", password='" + password + '\'' +
//                ", roles=" + roles +
                ", providerId='" + providerId + '\'' +
                ", providerType=" + providerType +
                '}';
    }
}
