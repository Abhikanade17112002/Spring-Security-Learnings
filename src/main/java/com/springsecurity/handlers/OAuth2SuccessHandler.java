package com.springsecurity.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.entity.LogInResponseDTO;
import com.springsecurity.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    @Lazy
    private UserService userService ;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication ;
        System.out.println("oAuth2AuthenticationToken ==> " + oAuth2AuthenticationToken);
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal() ;
        System.out.println("oAuth2User ==> " + oAuth2User);


        String registrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId() ;
        System.out.println(" registrationId ==> " +  registrationId );


        ResponseEntity<LogInResponseDTO> logInResponseDTOResponseEntity = userService.handleOAuth2LoginRequest( oAuth2User , registrationId ) ;


        LogInResponseDTO loginDTO = logInResponseDTOResponseEntity.getBody();
        if (loginDTO != null) {
            // Example: writing JWT token to response
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(loginDTO));
            response.getWriter().flush();
        } else {
            response.sendRedirect("/login?error");
        }
    }
}
