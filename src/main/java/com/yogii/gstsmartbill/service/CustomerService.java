
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.dto.CustomerRequest;
import com.yogii.gstsmartbill.dto.CustomerResponse;
import com.yogii.gstsmartbill.entity.Customer;
import com.yogii.gstsmartbill.entity.CustomerType;
import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.exception.DuplicateResourceException;
import com.yogii.gstsmartbill.exception.ResourceNotFoundException;
import com.yogii.gstsmartbill.repository.CustomerRepository;
import com.yogii.gstsmartbill.repository.TenantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;
    private final CurrentUserService currentUserService;

    public CustomerService(
            CustomerRepository customerRepository,
            TenantRepository tenantRepository,
            CurrentUserService currentUserService) {

        this.customerRepository = customerRepository;
        this.tenantRepository = tenantRepository;
        this.currentUserService = currentUserService;
    }

    // 1. CREATE CUSTOMER
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {

        Long tenantId = currentUserService.getCurrentTenantId();

        String email = normalizeEmail(request.getEmail());
        String gstin = normalizeGstin(request.getGstin());

        validateCustomerType(request.getCustomerType(), gstin);

        // Check duplicate email within the same tenant
        if (email != null &&
                customerRepository.existsByEmailAndTenant_Id(email, tenantId)) {

            throw new DuplicateResourceException(
                    "Customer email already exists");
        }

        // Check duplicate GSTIN within the same tenant
        if (gstin != null &&
                customerRepository.existsByGstinAndTenant_Id(gstin, tenantId)) {

            throw new DuplicateResourceException(
                    "Customer GSTIN already exists");
        }

        // Get logged-in shop
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tenant not found"));

        Customer customer = new Customer();

        customer.setTenant(tenant);
        customer.setCustomerName(request.getCustomerName().trim());
        customer.setPhone(normalize(request.getPhone()));
        customer.setEmail(email);
        customer.setGstin(gstin);
        customer.setCustomerType(request.getCustomerType());
        customer.setAddress(normalize(request.getAddress()));
        customer.setCity(normalize(request.getCity()));
        customer.setState(normalize(request.getState()));
        customer.setPincode(normalize(request.getPincode()));
        customer.setActive(true);

        Customer savedCustomer = customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    // 2. GET ALL CUSTOMERS
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {

        Long tenantId = currentUserService.getCurrentTenantId();

        return customerRepository
                .findByTenant_IdAndActiveTrue(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 3. GET CUSTOMER BY ID
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Customer customer = getCustomer(id, tenantId);

        return mapToResponse(customer);
    }

    // 4. SEARCH CUSTOMERS BY NAME
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String search) {

        Long tenantId = currentUserService.getCurrentTenantId();

        String keyword = search == null ? "" : search.trim();

        return customerRepository
                .findByTenant_IdAndActiveTrueAndCustomerNameContainingIgnoreCase(
                        tenantId, keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 5. UPDATE CUSTOMER
    @Transactional
    public CustomerResponse updateCustomer(
            Long id, CustomerRequest request) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Customer customer = getCustomer(id, tenantId);

        String email = normalizeEmail(request.getEmail());
        String gstin = normalizeGstin(request.getGstin());

        validateCustomerType(request.getCustomerType(), gstin);

        // Check duplicate email excluding current customer
        if (email != null &&
                customerRepository.existsByEmailAndTenant_IdAndIdNot(
                        email, tenantId, id)) {

            throw new DuplicateResourceException(
                    "Customer email already exists");
        }

        // Check duplicate GSTIN excluding current customer
        if (gstin != null &&
                customerRepository.existsByGstinAndTenant_IdAndIdNot(
                        gstin, tenantId, id)) {

            throw new DuplicateResourceException(
                    "Customer GSTIN already exists");
        }

        customer.setCustomerName(request.getCustomerName().trim());
        customer.setPhone(normalize(request.getPhone()));
        customer.setEmail(email);
        customer.setGstin(gstin);
        customer.setCustomerType(request.getCustomerType());
        customer.setAddress(normalize(request.getAddress()));
        customer.setCity(normalize(request.getCity()));
        customer.setState(normalize(request.getState()));
        customer.setPincode(normalize(request.getPincode()));

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }

    // 6. DELETE CUSTOMER (SOFT DELETE)
    @Transactional
    public void deleteCustomer(Long id) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Customer customer = getCustomer(id, tenantId);

        customer.setActive(false);

        customerRepository.save(customer);
    }

    // 7. FIND CUSTOMER WITH TENANT SECURITY
    private Customer getCustomer(Long id, Long tenantId) {

        Customer customer = customerRepository
                .findByIdAndTenant_Id(id, tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found"));

        if (!customer.isActive()) {
            throw new ResourceNotFoundException("Customer not found");
        }

        return customer;
    }

    // 8. B2B / B2C VALIDATION
    private void validateCustomerType(CustomerType type, String gstin) {

        if (type == null) {
            throw new IllegalArgumentException(
                    "Customer type is required");
        }

        if (type == CustomerType.B2B && gstin == null) {

            throw new IllegalArgumentException(
                    "GSTIN is required for B2B customers");
        }

        if (type == CustomerType.B2C && gstin != null) {

            throw new IllegalArgumentException(
                    "GSTIN should not be provided for B2C customers");
        }
    }

    // 9. NORMALIZE GENERAL TEXT
    private String normalize(String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    // 10. NORMALIZE EMAIL
    private String normalizeEmail(String email) {

        String value = normalize(email);

        return value == null ? null : value.toLowerCase();
    }

    // 11. NORMALIZE GSTIN
    private String normalizeGstin(String gstin) {

        String value = normalize(gstin);

        return value == null ? null : value.toUpperCase();
    }

    // 12. CONVERT ENTITY TO RESPONSE DTO
    private CustomerResponse mapToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setCustomerName(customer.getCustomerName());
        response.setPhone(customer.getPhone());
        response.setEmail(customer.getEmail());
        response.setGstin(customer.getGstin());
        response.setCustomerType(customer.getCustomerType());
        response.setAddress(customer.getAddress());
        response.setCity(customer.getCity());
        response.setState(customer.getState());
        response.setPincode(customer.getPincode());
        response.setActive(customer.isActive());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}
