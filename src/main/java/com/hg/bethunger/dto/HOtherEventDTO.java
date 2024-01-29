package com.hg.bethunger.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HOtherEventDTO extends HappenedEventDTO {
    private PlayerInfoDTO player;
    private String message;
}
