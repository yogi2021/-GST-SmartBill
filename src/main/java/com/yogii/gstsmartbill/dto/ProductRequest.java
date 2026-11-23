// this product used for..to come product data from frontent
//then recieve it and validate it..


package com.yogii.gstsmartbill.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name cannot exceed 150 characters")
    private String productName;

    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @Size(max = 20, message = "HSN code cannot exceed 20 characters")
    private String hsnCode;

    @NotNull(message = "GST rate is required")
    @DecimalMin(value = "0.00", message = "GST rate cannot be negative")
    @DecimalMax(value = "100.00", message = "GST rate cannot exceed 100")
    private BigDecimal gstRate;

    @NotNull(message = "Purchase price is required")
    @DecimalMin(value = "0.00", message = "Purchase price cannot be negative")
    private BigDecimal purchasePrice;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.00", message = "Selling price cannot be negative")
    private BigDecimal sellingPrice;

    @NotNull(message = "Stock quantity is required")
    @DecimalMin(value = "0.000", message = "Stock cannot be negative")
    private BigDecimal stockQuantity;

    @NotNull(message = "Minimum stock level is required")
    @DecimalMin(value = "0.000", message = "Minimum stock cannot be negative")
    private BigDecimal minStockLevel;

    // Getters and Setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public BigDecimal getGstRate() {
        return gstRate;
    }

    public void setGstRate(BigDecimal gstRate) {
        this.gstRate = gstRate;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(BigDecimal minStockLevel) {
        this.minStockLevel = minStockLevel;
    }
}
