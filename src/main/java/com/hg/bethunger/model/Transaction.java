package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.TransactionStatus;
import com.hg.bethunger.model.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus status;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime committed_at;

    private String paymentSource;

    @ManyToOne
    @JoinColumn(name = "bet_id")
    private Bet bet;

    @OneToOne(mappedBy = "transaction")
    private Supply supply;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}