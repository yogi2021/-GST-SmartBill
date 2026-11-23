
// this product repository used for communicate repository of product to database

package com.yogii.gstsmartbill.repository;

import com.yogii.gstsmartbill.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get all products belonging to a tenant
    List<Product> findByTenant_Id(Long tenantId);

    // Get active products belonging to a tenant
    List<Product> findByTenant_IdAndActiveTrue(Long tenantId);

    // Find product by ID and tenant ID
    Optional<Product> findByIdAndTenant_Id(Long id, Long tenantId);

    // Check duplicate SKU within a tenant
    boolean existsBySkuAndTenant_Id(String sku, Long tenantId);

    // Search products by name within a tenant
    List<Product> findByTenant_IdAndProductNameContainingIgnoreCase(
            Long tenantId,
            String productName
    );
}
