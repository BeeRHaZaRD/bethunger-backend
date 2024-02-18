package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.Sex;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerOddDTO {
    private Integer district;
    private Sex sex;
    private Double odd;
}
