package com.booking.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Setter
@Getter
public class BookSlotRequest {
    @NotNull(message = "Slot ID is required")
    private Long slotId;
}
