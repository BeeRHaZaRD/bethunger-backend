package com.hg.bethunger.controller;

import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.dto.PlayerInfoDTO;
import com.hg.bethunger.dto.TrainResultsDTO;
import com.hg.bethunger.model.enums.Sex;
import com.hg.bethunger.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path = "/{playerId}")
    public PlayerDTO getPlayerById(@PathVariable Long playerId) {
        return playerService.getPlayerById(playerId);
    }

    @GetMapping
    public List<PlayerDTO> getPlayers() {
        return playerService.getPlayers();
    }

    @GetMapping(path = "/available")
    public List<PlayerInfoDTO> getAvailablePlayers(@RequestParam Integer district, @RequestParam Sex sex) {
        return playerService.getAvailablePlayers(district, sex);
    }

    @PutMapping(path = "/{playerId}/trainResults")
    public void updateTrainResults(@PathVariable Long playerId, @RequestBody @Valid TrainResultsDTO dto) {
        playerService.updateTrainResults(playerId, dto);
    }
}
