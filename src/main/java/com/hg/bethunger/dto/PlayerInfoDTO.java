package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlayerInfoDTO {
    private Long id;
    private String fullName;
    private LocalDate birthDate;
    private Integer district;
    private Sex sex;
    private Double coefficient;
    private PlayerStatus status;
}
