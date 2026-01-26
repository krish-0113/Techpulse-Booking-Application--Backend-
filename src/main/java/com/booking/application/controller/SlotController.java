package com.booking.application.controller;

import com.booking.application.dtos.request.CreateSlotRequest;
import com.booking.application.dtos.response.SlotResponse;
import com.booking.application.service.SlotService;
import com.booking.application.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController {

    private final SlotService slotService;

    /**
     * ================= CREATE SLOT =================
     * POST /slots
     * Role: ADMIN
     *
     * - Admin creates a new slot
     * - Slot status is automatically set to AVAILABLE
     * - Overlapping & duplicate checks handled in service
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SlotResponse>> createSlot(
            @Valid @RequestBody CreateSlotRequest request
    ) {
        SlotResponse slotResponse = slotService.createSlot(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Slot created successfully",
                        slotResponse
                )
        );
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<SlotResponse>>> getAllSlots() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Slots fetched successfully",
                        slotService.getAllSlots()
                )
        );
    }
}
