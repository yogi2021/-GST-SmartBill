
package com.yogii.gstsmartbill.controller;

import com.yogii.gstsmartbill.dto.GstCalculationRequest;
import com.yogii.gstsmartbill.dto.GstCalculationResponse;
import com.yogii.gstsmartbill.service.GstCalculationService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gst")
public class GstCalculationController {

    private final GstCalculationService gstCalculationService;

    public GstCalculationController(
            GstCalculationService gstCalculationService) {

        this.gstCalculationService = gstCalculationService;

    }

    // CALCULATE GST
    @PostMapping("/calculate")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'BILLING_STAFF')")
    public ResponseEntity<GstCalculationResponse> calculateGST(
            @Valid @RequestBody GstCalculationRequest request) {

        GstCalculationResponse response =
                gstCalculationService.calculateGST(request);

        return ResponseEntity.ok(response);


    }
}
