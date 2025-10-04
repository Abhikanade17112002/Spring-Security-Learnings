package com.springsecurity.controller;


import com.springsecurity.dto.AddUserRequestDTO;
import com.springsecurity.entity.LogInRequestDTO;
import com.springsecurity.entity.LogInResponseDTO;
import com.springsecurity.entity.Roles;
import com.springsecurity.entity.User;
import com.springsecurity.repository.RoleRepository;
import com.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService ;

    @Autowired
    private RoleRepository roleRepository ;


    @PostMapping("/login")
    public ResponseEntity<LogInResponseDTO> getUserLoggedIn(@RequestBody LogInRequestDTO logInRequestDTO){

        return  ResponseEntity.status(HttpStatus.OK)
                .body(
                        userService.getUserLoggedIn( logInRequestDTO)
                ) ;
    }

    @PostMapping("/register")
    public ResponseEntity<User> getUserRegistered(@RequestBody AddUserRequestDTO user){

        System.out.println("USER ==> " + user);

        User newUser = new User() ;
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmailId(user.getEmailId());
        newUser.setPassword(user.getPassword());

        List<Roles> userRoles = user.getRoles()
                        .stream().map( (role )->

                        roleRepository.findByRoleName(role).orElse(null)


                        ).collect( Collectors.toList()) ;


        newUser.setRoles(userRoles);


        System.out.println("New Create User ==>  " + newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        userService.getUserRegistered( newUser )
                ) ;
    }
}
