
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.dto.LoginRequest;
import com.yogii.gstsmartbill.dto.LoginResponse;
import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.entity.User;
import com.yogii.gstsmartbill.repository.TenantRepository;
import com.yogii.gstsmartbill.repository.UserRepository;
import com.yogii.gstsmartbill.security.jwt.JwtService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            TenantRepository tenantRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String shopEmail = request.getShopEmail()
                .trim().toLowerCase();

        String userEmail = request.getUserEmail()
                .trim().toLowerCase();

        // 1. Find shop
        Tenant tenant = tenantRepository
                .findByEmail(shopEmail)
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid login credentials"
                        )
                );

        // 2. Find user inside that shop
        User user = userRepository
                .findByEmailAndTenant_Id(
                        userEmail,
                        tenant.getId()
                )
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid login credentials"
                        )
                );

        // 3. Verify password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new BadCredentialsException(
                    "Invalid login credentials"
            );
        }

        // 4. Generate JWT
        String token = jwtService.generateToken(user);

        // 5. Return safe response
        return new LoginResponse(
                token,
                user.getId(),
                tenant.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
