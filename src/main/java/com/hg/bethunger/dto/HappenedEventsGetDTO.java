package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HappenedEventsGetDTO {
    private LocalDateTime after;
}
