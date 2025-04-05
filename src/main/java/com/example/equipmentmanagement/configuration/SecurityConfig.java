package com.example.equipmentmanagement.configuration;

import com.example.equipmentmanagement.auth.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login").permitAll()
                        // user
                        .requestMatchers("/api/user/active", "/api/user/account", "/api/user/active-technicians", "/api/user/change-password").authenticated()
                        .requestMatchers("/api/user/**").hasRole("ADMIN")
                        // service request
                        .requestMatchers("/api/service-request/*/cancel", "/api/service-request/equipment/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/service-request").authenticated()
                        .requestMatchers("/api/service-request/**").hasAnyRole("TECHNICIAN", "ADMIN")
                        .requestMatchers("/api/service-request-note/**").hasAnyRole("TECHNICIAN", "ADMIN")
                        // transfer
                        .requestMatchers("/api/transfer/my-transfers", "/api/transfer/to-accept", "/api/transfer/*/accept", "/api/transfer/*/reject").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/transfer").authenticated()
                        .requestMatchers("/api/transfer/**").hasAnyRole("MANAGER", "ADMIN")
                        // address, equipment type
                        .requestMatchers(HttpMethod.GET, "/api/address/**", "/api/equipment-type/**").authenticated()
                        .requestMatchers("/api/address/**", "/api/equipment-type/**").hasRole("ADMIN")
                        // Equipment
                        .requestMatchers(HttpMethod.GET, "/api/equipment").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/equipment/**").authenticated()
                        .requestMatchers("/api/equipment/**").hasAnyRole("MANAGER", "ADMIN")
                        // other
                        .anyRequest().authenticated()
                )
                .build();
    }
}
