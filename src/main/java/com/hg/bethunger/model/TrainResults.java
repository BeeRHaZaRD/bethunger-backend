package com.hg.bethunger.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TrainResults {
    @NotNull
    private Integer strength = 0;

    @NotNull
    private Integer endurance = 0;

    @NotNull
    private Integer agility = 0;

    @NotNull
    private Integer stealth = 0;

    @NotNull
    private Integer steelArms = 0;

    @NotNull
    private Integer weapon = 0;

    @NotNull
    private Integer archery = 0;

    @NotNull
    private Integer handToHand = 0;

    public TrainResults(Integer strength, Integer endurance, Integer agility, Integer stealth, Integer steelArms, Integer weapon, Integer archery, Integer handToHand) {
        this.strength = strength;
        this.endurance = endurance;
        this.agility = agility;
        this.stealth = stealth;
        this.steelArms = steelArms;
        this.weapon = weapon;
        this.archery = archery;
        this.handToHand = handToHand;
    }
}
