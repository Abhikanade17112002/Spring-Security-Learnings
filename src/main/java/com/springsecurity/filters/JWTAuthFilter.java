package com.springsecurity.filters;

import com.springsecurity.entity.User;
import com.springsecurity.repository.UserRepository;
import com.springsecurity.utility.AuthUtility;
import jakarta.persistence.Column;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private AuthUtility authUtility ;
    @Autowired
    private UserRepository userRepository ;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("Incoming Request ==> {}"+request.getRequestURI());

    final String requestHeaderToken = request.getHeader("Authorization");
        System.out.println("requestHeaderToken ==> " +requestHeaderToken);

        if( requestHeaderToken == null || !requestHeaderToken.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return ;
        }

        final String token = requestHeaderToken.split("Bearer ")[1] ;
        System.out.println("Token ==> " + token);

        String retrivedUserName = authUtility.extractUsername(token);

        System.out.println("retrivedUserName ==> " + retrivedUserName);


        if( retrivedUserName != null && SecurityContextHolder.getContext().getAuthentication() == null ){
            User retrivedUser = (User) userRepository.findByUserName(retrivedUserName)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User With User Name Not Found: " + retrivedUserName));

            System.out.println("User Roles ==> " + retrivedUser.getAuthorities() );

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(retrivedUser,null,retrivedUser.getAuthorities()) ;
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        filterChain.doFilter(request,response);
        return ;
    }
}
