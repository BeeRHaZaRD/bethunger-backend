package com.hg.bethunger.dto.controlsystem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlannedEventRequestDTO {
    private Long id;
    private Long eventTypeId;
}
