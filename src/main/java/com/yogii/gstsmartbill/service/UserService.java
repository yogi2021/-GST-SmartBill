
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.entity.User;
import com.yogii.gstsmartbill.repository.TenantRepository;
import com.yogii.gstsmartbill.repository.UserRepository;
import com.yogii.gstsmartbill.security.user.UserRole;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor Injection
    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register shop and its owner
    @Transactional
    public User registerOwner(Tenant tenant, User user) {

        // Save shop first
        Tenant savedTenant = tenantRepository.save(tenant);

        // Connect owner with shop
        user.setTenant(savedTenant);

        // Assign OWNER role
        user.setRole(UserRole.OWNER);

        // Encrypt password using BCrypt
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Save owner
        return userRepository.save(user);
    }
}
