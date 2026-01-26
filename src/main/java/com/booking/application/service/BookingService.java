package com.booking.application.service;

import com.booking.application.dtos.response.BookingResponse;



public interface BookingService {
    BookingResponse bookSlot(Long slotId);
    void cancelBookingByUser(Long bookingId);
    void cancelBookingByAdmin(Long bookingId);
}

