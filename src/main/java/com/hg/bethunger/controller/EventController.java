package com.hg.bethunger.controller;

import com.hg.bethunger.dto.HappenedEventDTO;
import com.hg.bethunger.dto.PlannedEventCreateDTO;
import com.hg.bethunger.dto.PlannedEventDTO;
import com.hg.bethunger.dto.controlsystem.HappenedEventCreateDTO;
import com.hg.bethunger.mapper.PlannedEventMapper;
import com.hg.bethunger.model.PlannedEvent;
import com.hg.bethunger.service.EventService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/games/{gameId}")
public class EventController {
    private final EventService eventService;
    private final PlannedEventMapper plannedEventMapper;

    @Autowired
    public EventController(EventService eventService, PlannedEventMapper plannedEventMapper) {
        this.eventService = eventService;
        this.plannedEventMapper = plannedEventMapper;
    }

    @PostMapping(path = "/planned-events")
    @ResponseStatus(HttpStatus.CREATED)
    public PlannedEventDTO createPlannedEvent(@PathVariable Long gameId, @RequestBody @Valid PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent;
        if (dto.getStartAt() != null) {
            plannedEvent = eventService.createPlannedEvent(gameId, dto);
        } else {
            plannedEvent = eventService.runPlannedEvent(gameId, dto);
        }
        return plannedEventMapper.toDto(plannedEvent);
    }

    @DeleteMapping(path = "/planned-events/{plannedEventId}")
    public void removePlannedEvent(@PathVariable Long gameId, @PathVariable Long plannedEventId) {
        eventService.removePlannedEvent(gameId, plannedEventId);
    }

    @GetMapping(path = "/happened-events")
    @Validated
    public List<HappenedEventDTO> getHappenedEvents(
        @PathVariable Long gameId,
        @RequestParam(required = false) @PastOrPresent LocalDateTime after,
        @RequestParam(required = false) @Positive Long playerId
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

    @PostMapping(path = "/happened-events")
    @ResponseStatus(HttpStatus.CREATED)
    public void createHappenedEvent(@PathVariable Long gameId, @RequestBody HappenedEventCreateDTO dto) {
        eventService.createHappenedEvent(gameId, dto);
    }
}
