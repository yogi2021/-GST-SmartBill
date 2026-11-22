
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.service.TenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    // Register a new shop
    @PostMapping
    public Tenant registerTenant(@RequestBody Tenant tenant) {
        return tenantService.registerTenant(tenant);
    }

    // Get all shops
    @GetMapping
    public List<Tenant> getAllTenants() {
        return tenantService.getAllTenants();
    }

    // Get shop by ID
    @GetMapping("/{id}")
    public Tenant getTenantById(@PathVariable Long id) {
        return tenantService.getTenantById(id);
    }
}
