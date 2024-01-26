package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.HPlayerEventType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HPlayerEventDTO extends HappenedEventDTO {
    private PlayerDTO player;
    private HPlayerEventType playerEventType;
}
