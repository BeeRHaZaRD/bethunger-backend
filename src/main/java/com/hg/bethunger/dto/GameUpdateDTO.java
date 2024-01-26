package com.hg.bethunger.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GameUpdateDTO {
    private String name;
    private LocalDateTime dateStart;
    private String description;
    private String arenaType;
    private String arenaDescription;
}
