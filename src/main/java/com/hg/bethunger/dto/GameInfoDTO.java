package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.GameStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GameInfoDTO {
    private Long id;
    private String name;
    private UserInfoDTO manager;
    private GameStatus status;
    private LocalDateTime dateStart;
    private Long duration;
    private String description;
    private String arenaType;
    private String arenaDescription;
    private PlayerInfoDTO winner;
}
