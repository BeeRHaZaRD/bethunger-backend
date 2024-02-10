package com.hg.bethunger.controller;

import com.hg.bethunger.dto.*;
import com.hg.bethunger.dto.controlsystem.HappenedEventCreateDTO;
import com.hg.bethunger.exception.ResourceNotFoundException;
import com.hg.bethunger.mapper.PlannedEventMapper;
import com.hg.bethunger.model.PlannedEvent;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.security.UserPrincipal;
import com.hg.bethunger.service.EventService;
import com.hg.bethunger.service.GameService;
import jakarta.validation.Valid;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@CommonsLog
@RestController
@RequestMapping(path="/games")
public class GameController {
    private final GameService gameService;
    private final EventService eventService;
    private final PlannedEventMapper plannedEventMapper;

    @Autowired
    public GameController(GameService gameService, EventService eventService, PlannedEventMapper plannedEventMapper) {
        this.gameService = gameService;
        this.eventService = eventService;
        this.plannedEventMapper = plannedEventMapper;
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

//    @PostMapping(path = "/{gameId}/eventTypes")
//    public EventTypeDTO addEventType(@PathVariable Long gameId, @RequestBody EventTypeCreateDTO eventTypeCreateDTO) {
//        return gameService.addEventType(gameId, eventTypeCreateDTO);
//    }

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

    @PostMapping(path = "/{gameId}/plannedEvents")
    public PlannedEventDTO createPlannedEvent(@PathVariable Long gameId, @RequestBody @Valid PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent;
        if (dto.getStartAt() != null) {
            plannedEvent = eventService.createPlannedEvent(gameId, dto);
        } else {
            plannedEvent = eventService.runPlannedEvent(gameId, dto);
        }
        return plannedEventMapper.toDto(plannedEvent);
    }

    @DeleteMapping(path = "/{gameId}/plannedEvents/{plannedEventId}")
    public void removePlannedEvent(@PathVariable Long gameId, @PathVariable Long plannedEventId) {
        eventService.removePlannedEvent(gameId, plannedEventId);
    }

    @GetMapping(path = "/{gameId}/happenedEvents")
    public List<HappenedEventDTO> getHappenedEvents(
        @PathVariable Long gameId,
        @RequestParam(required = false) LocalDateTime after,
        @RequestParam(required = false) Long playerId
    ) {
        if (after != null && playerId != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid query parameters");
        } else if (after != null) {
            return eventService.getHappenedEventsAfter(gameId, after);
        } else if (playerId != null) {
            return eventService.getHappenedEventsByPlayer(gameId, playerId);
        } else {
            return eventService.getHappenedEvents(gameId);
        }
    }

    @PostMapping(path = "/{gameId}/happenedEvents")
    public void createHappenedEvent(@PathVariable Long gameId, @RequestBody HappenedEventCreateDTO dto) {
        eventService.createHappenedEvent(gameId, dto);
    }
}
