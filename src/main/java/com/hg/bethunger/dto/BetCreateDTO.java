package com.hg.bethunger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BetCreateDTO {
    private Long playerId;
    @DecimalMin(value = "10.0")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;
}
