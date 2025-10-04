package com.springsecurity.config;


import com.springsecurity.filters.JWTAuthFilter;
import com.springsecurity.handlers.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JWTAuthFilter jwtAuthFilter ;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http    .sessionManagement(sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/public/**","/auth/**","/admin/**").permitAll()
                                .requestMatchers("/admin/patients/**").hasRole("ADMIN")
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()

                )
                .formLogin((auth)->auth.disable())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth->oAuth
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler((request, response, exception) ->

                                System.out.println("ERROR ==> " + exception.getMessage())

                        ));
        ;

        return http.build() ;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }



//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1 = User.withUsername("admin")
//                .password("{noop}Admin@123")
//                .roles("ADMIN")
//                .build() ;
//
//        UserDetails user2 = User.withUsername("user")
//                .password("{noop}User@123")
//                .roles("USER")
//                .build() ;
//
//        return  new InMemoryUserDetailsManager(user2,user1) ;
//    }
}
