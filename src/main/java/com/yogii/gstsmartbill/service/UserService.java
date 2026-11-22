
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.dto.RegisterRequest;
import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.entity.User;
import com.yogii.gstsmartbill.exception.DuplicateResourceException;
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

    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerOwner(RegisterRequest request) {

        String shopEmail = request.getShopEmail().trim().toLowerCase();
        String ownerEmail = request.getOwnerEmail().trim().toLowerCase();

        if (tenantRepository.existsByEmail(shopEmail)) {
            throw new DuplicateResourceException(
                    "Shop email already registered"
            );
        }

        String gstin = request.getGstin();

        if (gstin != null && !gstin.isBlank()) {
            gstin = gstin.trim().toUpperCase();

            if (tenantRepository.existsByGstin(gstin)) {
                throw new DuplicateResourceException(
                        "GSTIN already registered"
                );
            }
        } else {
            gstin = null;
        }

        // Create Tenant
        Tenant tenant = new Tenant();
        tenant.setShopName(request.getShopName().trim());
        tenant.setEmail(shopEmail);
        tenant.setGstin(gstin);
        tenant.setPhone(request.getPhone());

        Tenant savedTenant = tenantRepository.save(tenant);

        // Create Owner
        User owner = new User();
        owner.setFullName(request.getOwnerName().trim());
        owner.setEmail(ownerEmail);
        owner.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        owner.setRole(UserRole.OWNER);
        owner.setTenant(savedTenant);

        return userRepository.save(owner);
    }
}
