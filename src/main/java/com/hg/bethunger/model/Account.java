package com.hg.bethunger.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Embeddable
public class Account {
    @NotNull
    private BigDecimal balance = BigDecimal.valueOf(0);

    public void addMoney(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void subtractMoney(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Недостаточно средств на счете");
        }
        this.balance = this.balance.subtract(amount);
    }
}