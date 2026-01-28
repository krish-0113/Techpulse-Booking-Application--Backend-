package com.booking.application.controller;

import com.booking.application.dtos.response.BookingResponse;
import com.booking.application.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        BookingController controller =
                new BookingController(bookingService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void bookSlot_success() throws Exception {

        BookingResponse response = BookingResponse.builder()
                .bookingId(1L)
                .slotId(1L)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        when(bookingService.bookSlot(1L)).thenReturn(response);

        mockMvc.perform(
                        post("/api/bookings")
                                .param("slotId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Slot booked successfully"))
                .andExpect(jsonPath("$.data.bookingId").value(1L));
    }

    @Test
    void cancelByUser_success() throws Exception {

        doNothing().when(bookingService).cancelBookingByUser(1L);

        mockMvc.perform(
                        post("/api/bookings/1/cancel")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking cancelled"));
    }

    @Test
    void cancelByAdmin_success() throws Exception {

        doNothing().when(bookingService).cancelBookingByAdmin(1L);

        mockMvc.perform(
                        post("/api/admin/bookings/1/cancel")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Booking cancelled by admin"));
    }
}
