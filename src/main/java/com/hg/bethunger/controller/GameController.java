package com.hg.bethunger.controller;

import com.hg.bethunger.dto.*;
import com.hg.bethunger.exception.ResourceNotFoundException;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.security.UserPrincipal;
import com.hg.bethunger.service.GameService;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CommonsLog
@RestController
@RequestMapping(path="/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public List<GameInfoDTO> getGames(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return switch (userPrincipal.getUser().getRole()) {
            case USER ->
                gameService.getPublicGames();
            case MANAGER, ADMIN ->
                gameService.getAllGames();
        };
    }

    @GetMapping(path = "/{gameId}")
    public GameFullDTO getGameById(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long gameId) {
        GameFullDTO gameFullDTO = gameService.getGameById(gameId);
        if (userPrincipal.getUser().getRole() == UserRole.USER && gameFullDTO.getStatus() == GameStatus.DRAFT) {
            throw new ResourceNotFoundException("Game", gameId);
        }
        return gameFullDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameFullDTO createGame(@RequestBody @Valid GameCreateDTO dto) {
        return gameService.createGame(dto);
    }

    @PutMapping(path = "/{gameId}")
    public GameFullDTO updateGame(@PathVariable Long gameId, @RequestBody @Valid GameUpdateDTO dto) {
        return gameService.updateGame(gameId, dto);
    }

    @PostMapping(path = "/{gameId}/publish")
    public void publishGame(@PathVariable Long gameId) {
        gameService.publishGame(gameId);
    }

    @PostMapping(path = "/{gameId}/start")
    public void startGame(@PathVariable Long gameId) {
        gameService.startGame(gameId);
    }

    @PostMapping(path = "/{gameId}/players/{playerId}")
    public PlayerDTO addPlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
        return gameService.addPlayer(gameId, playerId);
    }

    @DeleteMapping(path = "/{gameId}/players/{playerId}")
    public void removePlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
        gameService.removePlayer(gameId, playerId);
    }

    @PostMapping(path = "/{gameId}/items/{itemId}")
    public GameItemDTO addItem(@PathVariable Long gameId, @PathVariable Long itemId) {
        return gameService.addItem(gameId, itemId);
    }

    @DeleteMapping(path = "/{gameId}/items/{itemId}")
    public void removeItem(@PathVariable Long gameId, @PathVariable Long itemId) {
        gameService.removeItem(gameId, itemId);
    }
}
