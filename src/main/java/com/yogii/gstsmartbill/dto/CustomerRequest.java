
package com.yogii.gstsmartbill.dto;

import com.yogii.gstsmartbill.entity.CustomerType;
import jakarta.validation.constraints.*;

public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name cannot exceed 150 characters")
    private String customerName;

    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phone;

    @Email(message = "Enter a valid email address")
    @Size(max = 150)
    private String email;

    @Pattern(
            regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$",
            message = "Enter a valid GSTIN"
    )
    private String gstin;

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Pattern(
            regexp = "^[0-9]{6}$",
            message = "Pincode must contain 6 digits"
    )
    private String pincode;

    // Getters and Setters

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
