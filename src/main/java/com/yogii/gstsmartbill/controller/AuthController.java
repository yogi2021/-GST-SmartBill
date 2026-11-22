
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.dto.ApiResponse;
import com.yogii.gstsmartbill.dto.RegisterRequest;
import com.yogii.gstsmartbill.dto.LoginRequest;
import com.yogii.gstsmartbill.dto.LoginResponse;
import com.yogii.gstsmartbill.service.UserService;
import com.yogii.gstsmartbill.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(
            UserService userService,
            AuthService authService) {

        this.userService = userService;
        this.authService = authService;
    }

    // Register Shop and Owner
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(
            @Valid @RequestBody RegisterRequest request) {

        userService.registerOwner(request);

        ApiResponse<String> response = new ApiResponse<>(
                true,
                "Shop and Owner registered successfully",
                null
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse loginResponse = authService.login(request);

        ApiResponse<LoginResponse> response = new ApiResponse<>(
                true,
                "Login successful",
                loginResponse
        );

        return ResponseEntity.ok(response);
    }
}
