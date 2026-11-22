
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.service.TenantService;
import org.springframework.web.bind.annotation.*;

import com.yogii.gstsmartbill.dto.TenantRequest;
import jakarta.validation.Valid;

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
    public Tenant registerTenant(@Valid @RequestBody TenantRequest request) {

        Tenant tenant = new Tenant();

        tenant.setShopName(request.getShopName());
        tenant.setGstin(request.getGstin());
        tenant.setPhone(request.getPhone());
        tenant.setEmail(request.getEmail());

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
