package com.hg.bethunger.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Account {
    @NotNull
    private BigDecimal balance = BigDecimal.valueOf(0);
}