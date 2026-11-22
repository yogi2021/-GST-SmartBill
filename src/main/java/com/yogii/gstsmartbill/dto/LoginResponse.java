
package com.yogii.gstsmartbill.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private Long tenantId;
    private String email;
    private String role;

    public LoginResponse(
            String token,
            Long userId,
            Long tenantId,
            String email,
            String role) {

        this.token = token;
        this.tokenType = "Bearer";
        this.userId = userId;
        this.tenantId = tenantId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
