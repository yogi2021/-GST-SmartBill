
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.dto.CustomerRequest;
import com.yogii.gstsmartbill.dto.CustomerResponse;
import com.yogii.gstsmartbill.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // 1. CREATE CUSTOMER
    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. GET ALL CUSTOMERS
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'BILLING_STAFF')")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(
            @RequestParam(required = false) String search) {

        List<CustomerResponse> customers;

        if (search != null && !search.trim().isEmpty()) {
            customers = customerService.searchCustomers(search);
        } else {
            customers = customerService.getAllCustomers();
        }

        return ResponseEntity.ok(customers);
    }

    // 3. GET CUSTOMER BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'BILLING_STAFF')")
    public ResponseEntity<CustomerResponse> getCustomerById(
            @PathVariable Long id) {

        CustomerResponse response =
                customerService.getCustomerById(id);

        return ResponseEntity.ok(response);
    }

    // 4. UPDATE CUSTOMER
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response =
                customerService.updateCustomer(id, request);

        return ResponseEntity.ok(response);
    }

    // 5. DELETE CUSTOMER
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id) {

        customerService.deleteCustomer(id);

        return ResponseEntity.noContent().build();
    }
}
