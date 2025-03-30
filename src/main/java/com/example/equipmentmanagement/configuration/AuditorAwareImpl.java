package com.example.equipmentmanagement.configuration;

import com.example.equipmentmanagement.dto.UserAuthDto;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        UserAuthDto user = (UserAuthDto) authentication.getPrincipal();
        return Optional.of(user.getUsername());
    }
}
