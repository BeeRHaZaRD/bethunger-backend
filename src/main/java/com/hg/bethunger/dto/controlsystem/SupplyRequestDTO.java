package com.hg.bethunger.dto.controlsystem;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplyRequestDTO {
    private Long id;
    private Long playerId;
    private Long itemId;
}
