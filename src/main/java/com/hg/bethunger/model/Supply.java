package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.SupplyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "supplies")
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private SupplyStatus status = SupplyStatus.REQUESTED;

    @OneToOne(mappedBy = "supply")
    private HSupplyEvent hSupplyEvent;

    @OneToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supply supply = (Supply) o;
        return id.equals(supply.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}