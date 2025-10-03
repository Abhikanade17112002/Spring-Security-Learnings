package com.springsecurity.service;


import com.springsecurity.entity.LogInRequestDTO;
import com.springsecurity.entity.LogInResponseDTO;
import com.springsecurity.entity.User;
import com.springsecurity.repository.UserRepository;
import com.springsecurity.type.ProviderType;
import com.springsecurity.utility.AuthUtility;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

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
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // OAuth user: assign random encoded password
            String randPass = UUID.randomUUID().toString() ;
            System.out.println("GENERATED RANDOM PASSWORD ==> " + randPass);
            user.setPassword(passwordEncoder.encode(randPass));
        }
        user.setUserName(user.getFirstName()+"."+user.getLastName()+"@"+((int)(Math.random()*10000)));// ✅ Encrypt password
        return userRepository.save(user);
    }

    public ResponseEntity<LogInResponseDTO> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        ProviderType authProviderType = authUtility.getAuthProviderTypeFromRegistrationId(registrationId);

        System.out.println("authProviderType ==> " + authProviderType);

        String providerId = authUtility.getProviderIdFromOAuth2User(oAuth2User,registrationId) ;

        System.out.println("providerId ==> " + providerId);

        String userEmail = oAuth2User.getAttribute("email") ;

        System.out.println("userEmail ==> " + userEmail);

        User user = userRepository.findByProviderIdAndProviderTypeOrEmailId(providerId,authProviderType,userEmail).orElse(null);

        System.out.println("findByProviderIdAndProviderTypeOREmailId ==> " + user);


        if( user == null ){
            System.out.println("User No Where Found So This Is A Complete New User For Me ");
            System.out.println("Entering In The Signup Flow");


            Map<String, String> stringStringMap = authUtility.extractNameAndEmail(oAuth2User, registrationId);

            System.out.println("stringStringMap ==> " + stringStringMap);

            String fullName = stringStringMap.get("name");
            String email = stringStringMap.get("email");

// Split full name into firstName and lastName
            String firstName = "";
            String lastName = "";

            if (fullName != null && !fullName.isBlank()) {
                String[] nameParts = fullName.trim().split("\\s+");
                firstName = nameParts[0];  // first word as firstName

                if (nameParts.length > 1) {
                    // join remaining words as lastName
                    lastName = String.join(" ", java.util.Arrays.copyOfRange(nameParts, 1, nameParts.length));
                }
            }

            System.out.println("First Name: " + firstName);
            System.out.println("Last Name: " + lastName);
            System.out.println("Email: " + email);

            User newUser = new User() ;
            newUser.setEmailId(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setUserRoles("USER");
            User signedUpUser = getUserRegistered( newUser) ;

            System.out.println("signedUpUser ==> " + signedUpUser);


            LogInResponseDTO logInResponseDTO = new LogInResponseDTO(signedUpUser.getUserName(),
                    authUtility.generateToken(
                            signedUpUser
                    )

                    ) ;


            System.out.println("logInResponseDTO ==> " + logInResponseDTO);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(logInResponseDTO);



        }
        else{


            System.out.println("I Have My Old User  == > " + user);

            LogInResponseDTO logInResponseDTO = new LogInResponseDTO(
                    user.getUserName() ,
                    authUtility.generateToken(
                            user
                    )
            );

            System.out.println("logInResponseDTO exixts ==> " + logInResponseDTO);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(logInResponseDTO);




        }



    }
}
