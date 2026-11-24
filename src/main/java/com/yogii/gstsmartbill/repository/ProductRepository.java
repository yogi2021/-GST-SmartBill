
package com.yogii.gstsmartbill.repository;

import com.yogii.gstsmartbill.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTenant_Id(Long tenantId);

    List<Product> findByTenant_IdAndActiveTrue(Long tenantId);

    Optional<Product> findByIdAndTenant_Id(Long id, Long tenantId);

    boolean existsBySkuAndTenant_Id(String sku, Long tenantId);

    boolean existsBySkuAndTenant_IdAndIdNot(
            String sku, Long tenantId, Long id);

    List<Product> findByTenant_IdAndProductNameContainingIgnoreCase(
            Long tenantId, String productName);

    // pagination : for 1 page per limited product
    Page<Product> findByTenant_IdAndActiveTrue(
            Long tenantId, Pageable pageable);

    Page<Product> findByTenant_IdAndActiveTrueAndProductNameContainingIgnoreCase(
            Long tenantId, String productName, Pageable pageable);


    @Query("""
    SELECT p FROM Product p
    WHERE p.tenant.id = :tenantId
    AND p.active = true
    AND p.stockQuantity <= p.minStockLevel
    ORDER BY p.stockQuantity ASC
""")
    List<Product> findLowStockProducts(
            @Param("tenantId") Long tenantId);
}
