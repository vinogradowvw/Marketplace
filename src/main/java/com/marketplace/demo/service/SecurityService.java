package com.marketplace.demo.service;

import com.marketplace.demo.domain.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public boolean canDeleteUser(Authentication authentication, Long userId) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_MODERATOR"))) {
                return true;
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId().equals(userId);
        }

        return false;
    }
}
