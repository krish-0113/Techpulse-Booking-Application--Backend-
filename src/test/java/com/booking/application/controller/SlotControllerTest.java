package com.booking.application.controller;

import com.booking.application.dtos.request.CreateSlotRequest;
import com.booking.application.dtos.response.SlotResponse;
import com.booking.application.enums.SlotStatus;
import com.booking.application.exceptions.CustomException;
import com.booking.application.exceptions.GlobalExceptionHandler;
import com.booking.application.service.SlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SlotControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SlotService slotService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SlotController slotController = new SlotController(slotService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(slotController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * ===============================
     * TEST 1: CREATE SLOT - SUCCESS
     * ===============================
     */
    @Test
    void createSlot_success() throws Exception {

        SlotResponse response = new SlotResponse();
        response.setId(1L);
        response.setStartTime(LocalDateTime.now());
        response.setEndTime(LocalDateTime.now().plusHours(1));
        response.setStatus(SlotStatus.AVAILABLE);

        when(slotService.createSlot(any(CreateSlotRequest.class)))
                .thenReturn(response);

        CreateSlotRequest request = new CreateSlotRequest();
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(1));

        mockMvc.perform(
                        post("/api/slots")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Slot created successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.status").value("AVAILABLE"));
    }

    /**
     * ==========================================
     * TEST 2: CREATE SLOT - VALIDATION FAILURE
     * startTime missing
     * ==========================================
     */
    @Test
    void createSlot_validationFail_whenStartTimeMissing() throws Exception {

        CreateSlotRequest request = new CreateSlotRequest();
        request.setEndTime(LocalDateTime.now().plusHours(1));

        mockMvc.perform(
                        post("/api/slots")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/api/slots"));
    }

    /**
     * ==========================================
     * TEST 3: CREATE SLOT - SERVICE EXCEPTION
     * ==========================================
     */
    @Test
    void createSlot_shouldFail_whenServiceThrowsException() throws Exception {

        when(slotService.createSlot(any(CreateSlotRequest.class)))
                .thenThrow(new CustomException("Slot already exists"));

        CreateSlotRequest request = new CreateSlotRequest();
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(1));

        mockMvc.perform(
                        post("/api/slots")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Slot already exists"))
                .andExpect(jsonPath("$.path").value("/api/slots"));
    }


    /**
     * ===============================
     * TEST 4: GET ALL SLOTS - SUCCESS
     * ===============================
     */
    @Test
    void getAllSlots_success_withData() throws Exception {

        SlotResponse slot1 = new SlotResponse();
        slot1.setId(1L);
        slot1.setStatus(SlotStatus.AVAILABLE);

        SlotResponse slot2 = new SlotResponse();
        slot2.setId(2L);
        slot2.setStatus(SlotStatus.BOOKED);

        when(slotService.getAllSlots())
                .thenReturn(List.of(slot1, slot2));

        mockMvc.perform(get("/api/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Slots fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[1].id").value(2L));
    }

    /**
     * =================================
     * TEST 5: GET ALL SLOTS - EMPTY LIST
     * =================================
     */
    @Test
    void getAllSlots_success_whenNoSlotsExist() throws Exception {

        when(slotService.getAllSlots())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/slots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
