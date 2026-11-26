
package com.yogii.gstsmartbill.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class GstCalculationRequest {

    @NotBlank(message = "Seller state code is required")
    private String sellerStateCode;

    @NotBlank(message = "Place of supply state code is required")
    private String placeOfSupplyStateCode;

    @Valid
    @NotEmpty(message = "At least one item is required")
    private List<GstItemRequest> items;

    public String getSellerStateCode() {
        return sellerStateCode;
    }

    public void setSellerStateCode(String sellerStateCode) {
        this.sellerStateCode = sellerStateCode;
    }

    public String getPlaceOfSupplyStateCode() {
        return placeOfSupplyStateCode;
    }

    public void setPlaceOfSupplyStateCode(String placeOfSupplyStateCode) {
        this.placeOfSupplyStateCode = placeOfSupplyStateCode;
    }

    public List<GstItemRequest> getItems() {
        return items;
    }

    public void setItems(List<GstItemRequest> items) {
        this.items = items;
    }
}
