package com.booking.application.service.impl;

import com.booking.application.dtos.request.CreateSlotRequest;
import com.booking.application.dtos.response.SlotResponse;
import com.booking.application.entity.Slot;
import com.booking.application.enums.SlotStatus;
import com.booking.application.exceptions.CustomException;
import com.booking.application.repository.SlotRepository;
import com.booking.application.service.SlotService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SlotServiceImpl
 * ----------------
 * Handles all business logic related to Slot operations.
 * Only ADMIN can create slots.
 */
@Service
public class SlotServiceImpl implements SlotService {

    // Repository to interact with Slot table
    private final SlotRepository slotRepository;

    // Constructor Injection (recommended in industry)
    public SlotServiceImpl(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    /**
     * Creates a new slot.
     * Steps followed:
     * 1. Validate input time
     * 2. Check exact duplicate slot
     * 3. Check overlapping slot
     * 4. Save slot with AVAILABLE status
     * 5. Convert Entity → Response DTO
     */
    @Override
    public SlotResponse createSlot(CreateSlotRequest request) {

        // ================= STEP 1 =================
        // Validate start & end time
        // Business rule: start time must be before end time
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new CustomException("Start time must be before end time");
        }

        // ================= STEP 2 =================
        // Check if an exact same slot already exists
        // Example: 10:00–11:00 already present
        if (slotRepository.existsByStartTimeAndEndTime(
                request.getStartTime(),
                request.getEndTime())) {

            throw new CustomException("Slot already exists for the given time");
        }

        // ================= STEP 3 =================
        // Check overlapping slots
        // Example:
        // Existing: 10:00–11:00
        // New:      10:30–11:30  ❌ overlap
        if (slotRepository.existsOverlappingSlot(
                request.getStartTime(),
                request.getEndTime())) {

            throw new CustomException("Slot time overlaps with existing slot");
        }

        // ================= STEP 4 =================
        // Create Slot entity
        // ADMIN does not send status → system decides
        // New slot is always AVAILABLE
        Slot slot = Slot.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(SlotStatus.AVAILABLE)
                .build();

        // Save slot to database
        Slot savedSlot = slotRepository.save(slot);

        // ================= STEP 5 =================
        // Convert Entity → DTO and return response
        return mapToResponse(savedSlot);
    }

    /**
     * Maps Slot entity to SlotResponse DTO
     * Reason:
     * - Avoid exposing entity directly
     * - Return only required fields to client
     */
    private SlotResponse mapToResponse(Slot slot) {
        SlotResponse slotResponse = new SlotResponse();
        slotResponse.setId(slot.getId());
        slotResponse.setStartTime(slot.getStartTime());
        slotResponse.setEndTime(slot.getEndTime());
        slotResponse.setStatus(slot.getStatus());
        return slotResponse;
    }

    @Override
    public List<SlotResponse> getAllSlots() {

        // ================= STEP 1 =================
        // Fetch all slots from database
        List<Slot> slots = slotRepository.findAll();

        // ================= STEP 2 =================
        // Convert Entity list → DTO list
        return slots.stream()
                .map(this::mapToResponse)
                .toList();
    }




}
