
package com.yogii.gstsmartbill.repository;

import java.util.Optional;

import com.yogii.gstsmartbill.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {

    // Check whether shop email already exists
    boolean existsByEmail(String email);

    // Check whether GSTIN already exists
    boolean existsByGstin(String gstin);

    // Find shop using its email during login
    Optional<Tenant> findByEmail(String email);
}
