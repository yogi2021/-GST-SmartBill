
package com.yogii.gstsmartbill.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class GstItemRequest {

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    @Digits(integer = 9, fraction = 3)
    private BigDecimal quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.00", message = "Price cannot be negative")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal unitPrice;

    @NotNull(message = "GST rate is required")
    @DecimalMin(value = "0.00")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal gstRate;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getGstRate() {
        return gstRate;
    }

    public void setGstRate(BigDecimal gstRate) {
        this.gstRate = gstRate;
    }
}
