
package com.yogii.gstsmartbill.service;

import com.yogii.gstsmartbill.dto.ProductRequest;
import com.yogii.gstsmartbill.dto.ProductResponse;
import com.yogii.gstsmartbill.entity.Product;
import com.yogii.gstsmartbill.entity.Tenant;
import com.yogii.gstsmartbill.exception.DuplicateResourceException;
import com.yogii.gstsmartbill.exception.ResourceNotFoundException;
import com.yogii.gstsmartbill.repository.ProductRepository;
import com.yogii.gstsmartbill.repository.TenantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final TenantRepository tenantRepository;
    private final CurrentUserService currentUserService;

    public ProductService(
            ProductRepository productRepository,
            TenantRepository tenantRepository,
            CurrentUserService currentUserService) {

        this.productRepository = productRepository;
        this.tenantRepository = tenantRepository;
        this.currentUserService = currentUserService;
    }

    // 1. Create Product
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        Long tenantId = currentUserService.getCurrentTenantId();

        String sku = normalizeSku(request.getSku());

        if (sku != null &&
                productRepository.existsBySkuAndTenant_Id(sku, tenantId)) {
            throw new DuplicateResourceException("SKU already exists");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Tenant not found"));

        Product product = new Product();

        product.setTenant(tenant);
        product.setProductName(request.getProductName());
        product.setSku(sku);
        product.setHsnCode(request.getHsnCode());
        product.setGstRate(request.getGstRate());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockLevel(request.getMinStockLevel());
        product.setActive(true);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    // 2. Get All Active Products
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        Long tenantId = currentUserService.getCurrentTenantId();

        return productRepository
                .findByTenant_IdAndActiveTrue(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 3. Get Product By ID
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Product product = getProduct(id, tenantId);

        return mapToResponse(product);
    }

    // 4. Update Product
    @Transactional
    public ProductResponse updateProduct(
            Long id, ProductRequest request) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Product product = getProduct(id, tenantId);

        String sku = normalizeSku(request.getSku());

        if (sku != null &&
                productRepository.existsBySkuAndTenant_IdAndIdNot(
                        sku, tenantId, id)) {
            throw new DuplicateResourceException("SKU already exists");
        }

        product.setProductName(request.getProductName());
        product.setSku(sku);
        product.setHsnCode(request.getHsnCode());
        product.setGstRate(request.getGstRate());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setMinStockLevel(request.getMinStockLevel());

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    // 5. Soft Delete Product
    @Transactional
    public void deleteProduct(Long id) {

        Long tenantId = currentUserService.getCurrentTenantId();

        Product product = getProduct(id, tenantId);

        product.setActive(false);

        productRepository.save(product);
    }

    // Helper method: Find product within current tenant
    private Product getProduct(Long id, Long tenantId) {

        Product product = productRepository
                .findByIdAndTenant_Id(id, tenantId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        if (!product.isActive()) {
            throw new ResourceNotFoundException("Product not found");
        }

        return product;
    }

    // Helper method: Convert Entity to Response DTO
    private ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setSku(product.getSku());
        response.setHsnCode(product.getHsnCode());
        response.setGstRate(product.getGstRate());
        response.setPurchasePrice(product.getPurchasePrice());
        response.setSellingPrice(product.getSellingPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setMinStockLevel(product.getMinStockLevel());
        response.setActive(product.isActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

    private String normalizeSku(String sku) {
        if (sku == null || sku.isBlank()) {
            return null;
        }
        return sku.trim().toUpperCase();
    }
}
