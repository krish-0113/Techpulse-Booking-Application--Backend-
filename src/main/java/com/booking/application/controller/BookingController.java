package com.booking.application.controller;

import com.booking.application.dtos.response.BookingResponse;
import com.booking.application.service.BookingService;
import com.booking.application.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // ================= BOOK SLOT =================
    @PostMapping("/bookings")
    public ResponseEntity<ApiResponse<BookingResponse>> bookSlot(
            @RequestParam Long slotId
    ) {
        BookingResponse response = bookingService.bookSlot(slotId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Slot booked successfully", response)
        );
    }

    // ================= USER CANCEL =================
    @PostMapping("/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelByUser(
            @PathVariable Long id
    ) {
        bookingService.cancelBookingByUser(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Booking cancelled", null)
        );
    }

    // ================= ADMIN CANCEL =================
    @PostMapping("/admin/bookings/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelByAdmin(
            @PathVariable Long id
    ) {
        bookingService.cancelBookingByAdmin(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Booking cancelled by admin", null)
        );
    }
}
