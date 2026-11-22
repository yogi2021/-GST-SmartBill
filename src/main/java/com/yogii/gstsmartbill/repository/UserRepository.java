
package com.yogii.gstsmartbill.repository;

import com.yogii.gstsmartbill.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check whether email already exists
    boolean existsByEmail(String email);

    // Find user by email within a specific shop
    Optional<User> findByEmailAndTenant_Id(String email, Long tenantId);

    // Check duplicate email within a specific shop
    boolean existsByEmailAndTenant_Id(String email, Long tenantId);
}
