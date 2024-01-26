package com.hg.bethunger.dto;

import com.hg.bethunger.model.enums.GameStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class GameFullDTO {
    private Long id;
    private String name;
    private UserDTO manager;
    private GameStatus status;
    private LocalDateTime dateStart;
    private String description;
    private String arenaType;
    private String arenaDescription;
    private Map<Integer, List<PlayerDTO>> players;
    private List<GameItemDTO> items;
    private List<EventTypeDTO> eventTypes;
    private List<PlannedEventDTO> plannedEvents;
    private List<HappenedEventDTO> happenedEvents;

    public static Map<Integer, List<PlayerDTO>> initPlayers() {
        Map<Integer, List<PlayerDTO>> players = new HashMap<>();
        for (int i = 1; i < 13; i++) {
            players.put(i, Arrays.asList(null, null));
        }
        return players;
    }
}
