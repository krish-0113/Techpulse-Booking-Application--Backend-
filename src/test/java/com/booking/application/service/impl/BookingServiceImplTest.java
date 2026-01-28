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
import com.booking.application.utils.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private SlotRepository slotRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;


    private User user;
    private Slot slot;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        slot = Slot.builder()
                .id(1L)
                .status(SlotStatus.AVAILABLE)
                .build();

        booking = Booking.builder()
                .id(1L)
                .user(user)
                .slot(slot)
                .status(BookingStatus.ACTIVE)
                .build();
    }



    @AfterEach
    void tearDown() {
    }

    @Test
    void bookSlot_success() {
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {

            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("test@gmail.com");

            when(slotRepository.findByIdForUpdate(1L))
                    .thenReturn(Optional.of(slot));

            when(userRepository.findByEmail("test@gmail.com"))
                    .thenReturn(Optional.of(user));

            BookingResponse response = bookingService.bookSlot(1L);

            assertNotNull(response);
            assertEquals(SlotStatus.BOOKED, slot.getStatus());
        }
    }


    @Test
    void bookSlot_shouldFail_whenSlotAlreadyBooked() {
        slot.setStatus(SlotStatus.BOOKED);

        when(slotRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(slot));

        CustomException ex = assertThrows(
                CustomException.class,
                () -> bookingService.bookSlot(1L)
        );

        assertEquals("Slot already booked", ex.getMessage());
    }



    @Test
    void cancelBookingByUser_success() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("test@gmail.com");

            bookingService.cancelBookingByUser(1L);

            assertEquals(BookingStatus.CANCELED, booking.getStatus());
            assertEquals(SlotStatus.AVAILABLE, booking.getSlot().getStatus());
        }
    }



    @Test
    void cancelBookingByUser_shouldFail_whenDifferentUser() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getCurrentUserEmail)
                    .thenReturn("other@gmail.com");

            assertThrows(
                    CustomException.class,
                    () -> bookingService.cancelBookingByUser(1L)
            );
        }
    }




    @Test
    void cancelBookingByAdmin_success() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));

        bookingService.cancelBookingByAdmin(1L);

        assertEquals(BookingStatus.CANCELED, booking.getStatus());
        assertEquals(SlotStatus.AVAILABLE, booking.getSlot().getStatus());
    }



}