package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.HappenedEventType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HappenedEventDTO {
    private Long id;
    private LocalDateTime happenedAt;
    private HappenedEventType type;
}
