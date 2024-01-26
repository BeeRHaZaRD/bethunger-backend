package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCreateDTO {
    private String name;
    private Long managerId;
}
