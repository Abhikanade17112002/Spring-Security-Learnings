package com.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table( name = "roles")
public class Roles {

    @Id
    @GeneratedValue( strategy =  GenerationType.UUID )
    private String roleId ;

    @Column( nullable = false )
    private String roleName ;


    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "RoleAuthorityMapping" ,
            joinColumns = @JoinColumn( name = "roleId") ,
            inverseJoinColumns = @JoinColumn(name = "authorityId")
    )
    @JsonManagedReference
    private List<Authorities> authorities = new ArrayList<>() ;


    public Roles() {
    }

    public Roles(String roleId, String roleName, List<Authorities> authorities) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.authorities = authorities;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Authorities> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authorities> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "roleId='" + roleId + '\'' +
                ", roleName='" + roleName + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
