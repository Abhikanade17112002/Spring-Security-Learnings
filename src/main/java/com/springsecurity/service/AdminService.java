package com.springsecurity.service;

import com.springsecurity.dto.AddRoleRequestDTO;
import com.springsecurity.entity.Authorities;
import com.springsecurity.entity.Roles;
import com.springsecurity.repository.AuthorityRepository;
import com.springsecurity.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private AuthorityRepository authorityRepository ;

    @Autowired
    private RoleRepository roleRepository ;

    public Authorities addNewAuthority(Authorities authoritie) {
        return authorityRepository.save(authoritie) ;
    }

    public List<Authorities> getAuthorities() {
        return authorityRepository.findAll() ;
    }

    public Roles addNewRole(AddRoleRequestDTO addRoleRequestDTO) {

        List<String> authorityIdsList = addRoleRequestDTO.getAuthorityIds() ;
        String roleName = addRoleRequestDTO.getRoleName() ;

        Roles newRole = new Roles() ;
        newRole.setRoleName(roleName);

        List<Authorities> list  = authorityRepository.findAllById(authorityIdsList) ;

        for( Authorities authoritie : list ){
            newRole.getAuthorities().add(authoritie) ;
            authoritie.getRoles().add(newRole) ;
        }
        return roleRepository.save(newRole) ;
    }

    public List<Roles> getRoles() {

        return roleRepository.findAll() ;
    }
}
