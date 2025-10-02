package com.springsecurity.service;


import com.springsecurity.entity.LogInRequestDTO;
import com.springsecurity.entity.LogInResponseDTO;
import com.springsecurity.entity.User;
import com.springsecurity.repository.UserRepository;
import com.springsecurity.utility.AuthUtility;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthUtility authUtility;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserName(username).orElseThrow(
                () -> new EntityNotFoundException("Entity With UserName ==> " + username + " Not Found")
        );
    }

    public LogInResponseDTO getUserLoggedIn(LogInRequestDTO logInRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInRequestDTO.getUserName(),
                        logInRequestDTO.getPassword()
                )
        );

        User authenticatedUser = (User) authentication.getPrincipal();

        String generatedToken = authUtility.generateToken(authenticatedUser);
        System.out.println("Generated Token ==> " + generatedToken);

        return new LogInResponseDTO(
                authenticatedUser.getUsername(),
                "Bearer " + generatedToken
        );
    }

    public User getUserRegistered(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserName(user.getFirstName()+"."+user.getLastName()+"@"+((int)(Math.random()*10000)));// ✅ Encrypt password
        return userRepository.save(user);
    }
}
