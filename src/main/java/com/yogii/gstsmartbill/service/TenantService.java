
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    // Register a new shop
    public Tenant registerTenant(Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    // Get all registered shops
    public List<Tenant> getAllTenants() {
        return tenantRepository.findAll();
    }

    // Get shop by ID
    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found with ID: " + id));
    }
}
