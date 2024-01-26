package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private Long id;
    private String fullName;
    private Integer district;
    private LocalDate birthDate;
    private Double coefficient;
    private Sex sex;
    private PlayerStatus status;
}
