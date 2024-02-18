package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.BetStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BetDTO {
    private Long id;
    private GameInfoDTO game;
    private PlayerInfoDTO player;
    private BigDecimal amount;
    private BetStatus status;
}
