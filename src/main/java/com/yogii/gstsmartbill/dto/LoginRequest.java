
package com.yogii.gstsmartbill.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Shop email is required")
    @Email(message = "Enter a valid shop email")
    private String shopEmail;

    @NotBlank(message = "User email is required")
    @Email(message = "Enter a valid user email")
    private String userEmail;

    @NotBlank(message = "Password is required")
    private String password;

    public String getShopEmail() {
        return shopEmail;
    }

    public void setShopEmail(String shopEmail) {
        this.shopEmail = shopEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
