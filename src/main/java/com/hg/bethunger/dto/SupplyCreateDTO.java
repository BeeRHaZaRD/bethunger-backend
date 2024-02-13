package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplyCreateDTO {
    private Long playerId;
    private Long itemId;
}
