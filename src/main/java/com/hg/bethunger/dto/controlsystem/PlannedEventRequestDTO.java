package com.hg.bethunger.dto.controlsystem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlannedEventRequestDTO {
    private Long id;
    private Long eventTypeId;
}
