package com.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "Authorities")
public class Authorities {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID )
    private String authoritiesId ;


    @Column( nullable = false )
    private String authorityName ;


    @ManyToMany( mappedBy = "authorities" )
    @JsonBackReference
    private List<Roles> roles = new ArrayList<>() ;


    public Authorities(String authoritiesId, String authorityName, List<Roles> roles) {
        this.authoritiesId = authoritiesId;
        this.authorityName = authorityName;
        this.roles = roles;
    }

    public Authorities() {
    }

    public String getAuthoritiesId() {
        return authoritiesId;
    }

    public void setAuthoritiesId(String authoritiesId) {
        this.authoritiesId = authoritiesId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "authoritiesId='" + authoritiesId + '\'' +
                ", authorityName='" + authorityName + '\'' +
                ", roles=" + roles +
                '}';
    }
}
