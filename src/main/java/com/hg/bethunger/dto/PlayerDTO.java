package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Integer district;
    private Sex sex;
    private Double coefficient;
    private PlayerStatus status;
    private LocalDateTime cooldownTo;
    private TrainResultsDTO trainResults;
}
