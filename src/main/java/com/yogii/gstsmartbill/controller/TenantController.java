
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.dto.ApiResponse;
import com.yogii.gstsmartbill.dto.TenantRequest;
import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.service.TenantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Tenant>> registerTenant(
            @Valid @RequestBody TenantRequest request) {

        Tenant tenant = new Tenant();

        tenant.setShopName(request.getShopName());
        tenant.setGstin(request.getGstin());
        tenant.setPhone(request.getPhone());
        tenant.setEmail(request.getEmail());

        Tenant savedTenant = tenantService.registerTenant(tenant);

        ApiResponse<Tenant> response = new ApiResponse<>(
                true,
                "Shop registered successfully",
                savedTenant
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all registered shops
    @GetMapping
    public ResponseEntity<ApiResponse<List<Tenant>>> getAllTenants() {

        List<Tenant> tenants = tenantService.getAllTenants();

        ApiResponse<List<Tenant>> response = new ApiResponse<>(
                true,
                "Tenants fetched successfully",
                tenants
        );

        return ResponseEntity.ok(response);
    }

    // Get shop by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Tenant>> getTenantById(
            @PathVariable Long id) {

        Tenant tenant = tenantService.getTenantById(id);

        ApiResponse<Tenant> response = new ApiResponse<>(
                true,
                "Tenant fetched successfully",
                tenant
        );

        return ResponseEntity.ok(response);
    }
}
