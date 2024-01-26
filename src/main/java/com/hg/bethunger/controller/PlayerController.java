package com.hg.bethunger.controller;

import com.hg.bethunger.dto.PlayerDTO;
import com.hg.bethunger.model.enums.Sex;
import com.hg.bethunger.service.PlayerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path = "/available")
    public List<PlayerDTO> getAvailablePlayers(@RequestParam Integer district, @RequestParam Sex sex) {
        System.out.println("test");
        return playerService.getAvailablePlayers(district, sex);
    }
}
