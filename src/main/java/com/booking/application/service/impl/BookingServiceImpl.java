package com.booking.application.service.impl;

import com.booking.application.dtos.response.BookingResponse;
import com.booking.application.entity.Booking;
import com.booking.application.entity.Slot;
import com.booking.application.entity.User;
import com.booking.application.enums.BookingStatus;
import com.booking.application.enums.SlotStatus;
import com.booking.application.exceptions.CustomException;
import com.booking.application.repository.BookingRepository;
import com.booking.application.repository.SlotRepository;
import com.booking.application.repository.UserRepository;
import com.booking.application.service.BookingService;
import com.booking.application.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookingServiceImpl implements BookingService {

    private final SlotRepository slotRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(
            SlotRepository slotRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository
    ) {
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }





    // ================= BOOK SLOT =================

    @Transactional
    public BookingResponse bookSlot(Long slotId) {

        // 1️⃣ Lock slot row (race condition prevention)
        Slot slot = slotRepository.findByIdForUpdate(slotId)
                .orElseThrow(() -> new CustomException("Slot not found"));

        // 2️⃣ Check availability
        if (slot.getStatus() == SlotStatus.BOOKED) {
            throw new CustomException("Slot already booked");
        }

        // 3️⃣ Get logged-in user from JWT
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        // 4️⃣ Create booking
        Booking booking = Booking.builder()
                .slot(slot)
                .user(user)
                .status(BookingStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        bookingRepository.save(booking);

        // 5️⃣ Update slot status
        slot.setStatus(SlotStatus.BOOKED);

        // 6️⃣ Response
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .slotId(slot.getId())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }




    // ================= USER CANCEL =================

    @Transactional
    public void cancelBookingByUser(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException("Booking not found"));

        String email = SecurityUtils.getCurrentUserEmail();

        if (!booking.getUser().getEmail().equals(email)) {
            throw new CustomException("You can cancel only your own booking");
        }

        cancelBooking(booking);
    }

    // ================= ADMIN CANCEL =================

    @Transactional
    public void cancelBookingByAdmin(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new CustomException("Booking not found"));

        cancelBooking(booking);
    }

    // ================= COMMON CANCEL LOGIC =================
    private void cancelBooking(Booking booking) {

        booking.setStatus(BookingStatus.CANCELED);
        booking.getSlot().setStatus(SlotStatus.AVAILABLE);
    }

}
