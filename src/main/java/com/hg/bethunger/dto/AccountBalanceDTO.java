package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountBalanceDTO {
    private BigDecimal balance;
}
