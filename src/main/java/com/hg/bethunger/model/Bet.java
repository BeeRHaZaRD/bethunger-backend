package com.hg.bethunger.model;

import com.hg.bethunger.model.enums.BetStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private BetStatus status;

    @OneToMany(mappedBy = "bet")
    private List<Transaction> transactions;
}