package com.booking.application.service;

import com.booking.application.dtos.request.CreateSlotRequest;
import com.booking.application.dtos.response.SlotResponse;
import com.booking.application.entity.Slot;
import com.booking.application.utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SlotService {
    SlotResponse createSlot(CreateSlotRequest request);
    List<SlotResponse> getAllSlots();

}
