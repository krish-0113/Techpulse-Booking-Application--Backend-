package com.booking.application.dtos.response;

import com.booking.application.enums.SlotStatus;
import jakarta.persistence.SecondaryTable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
public class SlotResponse {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SlotStatus status;
}
