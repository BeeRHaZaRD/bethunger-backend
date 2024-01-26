package com.hg.bethunger.controller;

import com.hg.bethunger.dto.*;
import com.hg.bethunger.service.EventService;
import com.hg.bethunger.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/games")
public class GameController {
    private final GameService gameService;
    private final EventService eventService;

    public GameController(GameService gameService, EventService eventService) {
        this.gameService = gameService;
        this.eventService = eventService;
    }

    @GetMapping
    public List<GameInfoDTO> getGames() {
        return gameService.getGames();
    }

    @GetMapping(path = "/{gameId}")
    public GameFullDTO getGameById(@PathVariable Long gameId) {
        return gameService.getGameById(gameId);
    }

    @PostMapping
    public GameInfoDTO createGame(@RequestBody GameCreateDTO gameCreateDTO) {
        return gameService.createGame(gameCreateDTO);
    }

    @PutMapping(path = "/{gameId}")
    public GameFullDTO updateGame(@PathVariable Long gameId, @RequestBody GameUpdateDTO gameUpdateDTO) {
        return gameService.updateGame(gameId, gameUpdateDTO);
    }

    @PostMapping(path = "/{gameId}/publish")
    public GameFullDTO publishGame(@PathVariable Long gameId) {
        return gameService.publishGame(gameId);
    }

    @PostMapping(path = "/{gameId}/start")
    public GameFullDTO startGame(@PathVariable Long gameId) {
        return gameService.startGame(gameId);
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
    public PlannedEventDTO createPlannedEvent(@PathVariable Long gameId, @RequestBody PlannedEventCreateDTO plannedEventCreateDTO) {
        return eventService.createPlannedEvent(gameId, plannedEventCreateDTO);
    }

    @DeleteMapping(path = "/{gameId}/plannedEvents/{plannedEventId}")
    public void removePlannedEvent(@PathVariable Long gameId, @PathVariable Long plannedEventId) {
        eventService.removePlannedEvent(gameId, plannedEventId);
    }

    @GetMapping(path = "/games/{gameId}/happenedEvents")
    public List<HappenedEventDTO> getHappenedEventsAfter(@PathVariable Long gameId, @RequestBody HappenedEventsGetDTO happenedEventsGetDTO) {
        return eventService.getHappenedEventsAfter(gameId, happenedEventsGetDTO);
    }

    @PostMapping(path = "/games/{gameId}/happenedEvents")
    public void createHappenedEvent(@PathVariable Long gameId, @RequestBody HappenedEventCreateDTO happenedEventCreateDTO) {
        eventService.createHappenedEvent(gameId, happenedEventCreateDTO);
    }
}
