package com.hg.bethunger.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HPlannedEventDTO extends HappenedEventDTO {
    private PlannedEventDTO plannedEvent;
}
