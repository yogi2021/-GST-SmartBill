
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.dto.ApiResponse;
import com.yogii.gstsmartbill.dto.RegisterRequest;
import com.yogii.gstsmartbill.entity.User;
import com.yogii.gstsmartbill.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
}
