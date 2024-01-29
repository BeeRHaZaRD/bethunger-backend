package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
public class TrainResultsDTO {
    @Range(min = 0, max = 10)
    private Integer strength;

    @Range(min = 0, max = 10)
    private Integer endurance;

    @Range(min = 0, max = 10)
    private Integer agility;

    @Range(min = 0, max = 10)
    private Integer stealth;

    @Range(min = 0, max = 10)
    private Integer steelArms;

    @Range(min = 0, max = 10)
    private Integer weapon;

    @Range(min = 0, max = 10)
    private Integer archery;

    @Range(min = 0, max = 10)
    private Integer handToHand;
}