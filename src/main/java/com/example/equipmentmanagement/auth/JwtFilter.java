package com.example.equipmentmanagement.auth;

import com.example.equipmentmanagement.exception.UserNotFoundException;
import com.example.equipmentmanagement.model.User;
import com.example.equipmentmanagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthProvider;
    private final UserRepository userRepository;

    public JwtFilter(UserAuthProvider userAuthProvider, UserRepository userRepository) {
        this.userAuthProvider = userAuthProvider;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            String jwt = header.substring(7);

            if (userAuthProvider.isTokenValid(jwt)) {
                String username = userAuthProvider.getUsernameFromToken(jwt);
                User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username));

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
