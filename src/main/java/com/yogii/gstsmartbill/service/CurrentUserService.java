
// when shop owner login with thei tenant, then access data only their not another shop id..
// multi-tenant architecture

package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.entity.User;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    // Get currently authenticated user
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof User user)) {

            throw new AuthenticationCredentialsNotFoundException(
                    "User is not authenticated"
            );
        }

        return user;
    }

    // Get current user's ID
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    // Get current user's Tenant ID
    public Long getCurrentTenantId() {
        return getCurrentUser().getTenant().getId();
    }

    // Get current user's role
    public String getCurrentUserRole() {
        return getCurrentUser().getRole().name();
    }
}
