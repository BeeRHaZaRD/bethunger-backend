package com.hg.bethunger.dto.controlsystem;

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

    public static HappenedEventCreateDTO createOther(Long playerId, String message) {
        var dto = createOther(message);
        dto.setPlayerId(playerId);
        return dto;
    }

    public static HappenedEventCreateDTO createOther(String message) {
        var dto = new HappenedEventCreateDTO();
        dto.setType(HappenedEventType.OTHER);
        dto.setMessage(message);
        return dto;
    }
}
