package com.example.equipmentmanagement.configuration;

import com.example.equipmentmanagement.auth.JwtFilter;
import com.example.equipmentmanagement.auth.UserAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthProvider userAuthProvider;

    public SecurityConfig(UserAuthProvider userAuthProvider) {
        this.userAuthProvider = userAuthProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtFilter(userAuthProvider), BasicAuthenticationFilter.class)
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
                );

        return http.build();
    }
}
