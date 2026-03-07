package com.example.JavaDrive.Security;

import com.example.JavaDrive.Users.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JWTAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf((csrf -> csrf.disable()))
                .cors((cors -> cors.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/home", "/static/upload.html").hasAuthority("USER_ROLE")
                        .requestMatchers("/register","/login","/emailConfirmation",
                                "/emailVerify/{emailToken}", "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-resources/**", "/v3/api-docs/**",
                                "/v3/api-docs/**","/static/**","/error","/favicon.ico").permitAll()
                        .requestMatchers("/dashboard","/upload").hasAuthority("USER_ROLE")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)));
        return http.build();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        daoAuthProvider.setUserDetailsService(userService);
        return daoAuthProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
