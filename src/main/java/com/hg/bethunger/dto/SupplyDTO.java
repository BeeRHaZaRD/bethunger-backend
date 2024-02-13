package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SupplyDTO {
    private PlayerInfoDTO player;
    private ItemDTO item;
    private BigDecimal amount;
}
