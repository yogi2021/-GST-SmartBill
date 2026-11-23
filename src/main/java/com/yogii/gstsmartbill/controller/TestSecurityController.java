
package com.yogii.gstsmartbill.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestSecurityController {

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    public String ownerAccess() {
        return "Welcome Owner! Access Granted.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public String managerAccess() {
        return "Manager Access Granted.";
    }

    @GetMapping("/billing")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'BILLING_STAFF')")
    public String billingAccess() {
        return "Billing Access Granted.";
    }
}
