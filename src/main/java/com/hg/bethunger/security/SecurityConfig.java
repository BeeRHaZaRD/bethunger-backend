package com.hg.bethunger.security;

import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.service.UserDetailsServiceImpl;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@CommonsLog
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTFilter jwtFilter;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JWTFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> req
                .requestMatchers("/auth/register", "/auth/login").permitAll()
                .requestMatchers("/users/**").hasRole(UserRole.ADMIN.getRole())

                .requestMatchers(HttpMethod.POST, "/games").hasRole(UserRole.ADMIN.getRole())
                .requestMatchers(HttpMethod.POST, "/games/**/happenedEvents").hasRole(UserRole.ADMIN.getRole())
                .requestMatchers(HttpMethod.GET, "/games/**").authenticated()
                .requestMatchers("/games/**").hasAnyRole(UserRole.MANAGER.getRole(), UserRole.ADMIN.getRole())

                .requestMatchers(HttpMethod.GET, "/items/**").hasAnyRole(UserRole.MANAGER.getRole(), UserRole.ADMIN.getRole())
                .requestMatchers("/items/**").hasAnyRole(UserRole.ADMIN.getRole())

                .requestMatchers(HttpMethod.GET, "/players/**").hasAnyRole(UserRole.MANAGER.getRole(), UserRole.ADMIN.getRole())
                .requestMatchers(HttpMethod.GET, "/players/**/available").hasAnyRole(UserRole.MANAGER.getRole(), UserRole.ADMIN.getRole())
                .requestMatchers(HttpMethod.PUT, "/players/**/trainResults").hasAnyRole(UserRole.MANAGER.getRole(), UserRole.ADMIN.getRole())
                .requestMatchers("/players/**").hasAnyRole(UserRole.ADMIN.getRole())

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
