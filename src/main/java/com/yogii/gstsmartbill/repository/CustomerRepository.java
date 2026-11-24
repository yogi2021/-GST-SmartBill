
package com.yogii.gstsmartbill.repository;

import com.yogii.gstsmartbill.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    // Get all customers of a particular tenant
    List<Customer> findByTenant_Id(Long tenantId);

    // Get only active customers
    List<Customer> findByTenant_IdAndActiveTrue(Long tenantId);

    // Find customer by ID and tenant
    Optional<Customer> findByIdAndTenant_Id(
            Long id, Long tenantId);

    // Check duplicate email within same tenant
    boolean existsByEmailAndTenant_Id(
            String email, Long tenantId);

    // Check duplicate GSTIN within same tenant
    boolean existsByGstinAndTenant_Id(
            String gstin, Long tenantId);

    // Search customers by name
    List<Customer> findByTenant_IdAndActiveTrueAndCustomerNameContainingIgnoreCase(
            Long tenantId, String customerName);
}
