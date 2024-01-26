package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.HPlayerEventType;
import com.hg.bethunger.model.enums.HappenedEventType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HappenedEventCreateDTO {
    private HappenedEventType type;
    private Long playerId;
    private HPlayerEventType playerEventType;
    private Long plannedEventId;
    private Long supplyId;
    private String message;
}
