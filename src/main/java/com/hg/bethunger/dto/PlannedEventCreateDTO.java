package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlannedEventCreateDTO {
    private Long eventTypeId;
    private LocalDateTime startAt;
}
