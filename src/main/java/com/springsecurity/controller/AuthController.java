package com.springsecurity.controller;


import com.springsecurity.entity.LogInRequestDTO;
import com.springsecurity.entity.LogInResponseDTO;
import com.springsecurity.entity.User;
import com.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService ;


    @PostMapping("/login")
    public ResponseEntity<LogInResponseDTO> getUserLoggedIn(@RequestBody LogInRequestDTO logInRequestDTO){

        return  ResponseEntity.status(HttpStatus.OK)
                .body(
                        userService.getUserLoggedIn( logInRequestDTO)
                ) ;
    }

    @PostMapping("/register")
    public ResponseEntity<User> getUserRegistered(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        userService.getUserRegistered( user )
                ) ;
    }
}
