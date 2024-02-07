package com.hg.bethunger.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountOperationDTO {
    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "100000.0")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;
}
