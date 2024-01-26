package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.PlannedEventStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlannedEventDTO {
    private Long id;
    private EventTypeDTO eventType;
    private LocalDateTime startAt;
    private PlannedEventStatus status;
}
