package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.*;
import com.hg.bethunger.mapper.HappenedEventMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlannedEventMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.HPlayerEventType;
import com.hg.bethunger.model.enums.PlannedEventStatus;
import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.SupplyStatus;
import com.hg.bethunger.repository.*;
import com.hg.bethunger.scheduler.StartEventTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final PlannedEventMapper plannedEventMapper;
    private final PlannedEventRepository plannedEventRepository;
    private final ThreadPoolTaskScheduler taskScheduler;
    private final GameRepository gameRepository;
    private final HappenedEventMapper happenedEventMapper;
    private final HappenedEventRepository happenedEventRepository;
    private final PlayerRepository playerRepository;
    private final SupplyRepository supplyRepository;
    private final EventTypeRepository eventTypeRepository;

    private final Map<Long, ScheduledFuture<?>> scheduledEvents = new HashMap<>();

    public EventService(PlannedEventMapper plannedEventMapper, PlannedEventRepository plannedEventRepository, ThreadPoolTaskScheduler threadPoolTaskScheduler, GameRepository gameRepository, HappenedEventMapper happenedEventMapper, HappenedEventRepository happenedEventRepository, PlayerRepository playerRepository, SupplyRepository supplyRepository, EventTypeRepository eventTypeRepository) {
        this.plannedEventMapper = plannedEventMapper;
        this.plannedEventRepository = plannedEventRepository;
        this.taskScheduler = threadPoolTaskScheduler;
        this.gameRepository = gameRepository;
        this.happenedEventMapper = happenedEventMapper;
        this.happenedEventRepository = happenedEventRepository;
        this.playerRepository = playerRepository;
        this.supplyRepository = supplyRepository;
        this.eventTypeRepository = eventTypeRepository;
    }

    public List<HappenedEventDTO> getHappenedEventsAfter(Long gameId, HappenedEventsGetDTO dto) {
        return MappingUtils.mapList(
            happenedEventRepository.findAllByGameIdAndHappenedAtAfterOrderByHappenedAtDesc(gameId, dto.getAfter()), happenedEventMapper::toDTO
        );
    }

    @Transactional
    public void createHappenedEvent(Long gameId, HappenedEventCreateDTO dto) {
        HappenedEvent happenedEvent = happenedEventMapper.toEntity(dto);

        Game game = Utils.findOrThrow(gameRepository, gameId, "Game");
        happenedEvent.setGame(game);

        switch (happenedEvent.getType()) {
            case PLAYER -> {
                HPlayerEvent hPlayerEvent = (HPlayerEvent) happenedEvent;
                Player player = Utils.findOrThrow(playerRepository, dto.getPlayerId(), "Player");
                hPlayerEvent.setPlayer(player);
            }
            case PLANNED_EVENT -> {
                HPlannedEvent hPlannedEvent = (HPlannedEvent) happenedEvent;
                PlannedEvent plannedEvent = Utils.findOrThrow(plannedEventRepository, dto.getPlannedEventId(), "Planned event");
                hPlannedEvent.setPlannedEvent(plannedEvent);
            }
            case SUPPLY -> {
                HSupplyEvent supplyEvent = (HSupplyEvent) happenedEvent;
                Supply supply = Utils.findOrThrow(supplyRepository, dto.getSupplyId(), "Supply");
                supplyEvent.setSupply(supply);
            }
            case OTHER -> {
                HOtherEvent hOtherEvent = (HOtherEvent) happenedEvent;
                if (dto.getPlayerId() != null) {
                    Player player = Utils.findOrThrow(playerRepository, dto.getPlayerId(), "Player");
                    hOtherEvent.setPlayer(player);
                }
            }
        }
        happenedEvent = happenedEventRepository.save(happenedEvent);
        handleHappenedEvent(happenedEvent);
    }

    @Transactional
    public void handleHappenedEvent(HappenedEvent happenedEvent) {
        switch (happenedEvent.getType()) {
            case PLAYER -> {
                HPlayerEvent hPlayerEvent = (HPlayerEvent) happenedEvent;
                if (hPlayerEvent.getPlayerEventType() == HPlayerEventType.KILLED) {
                    Player player = hPlayerEvent.getPlayer();
                    player.setStatus(PlayerStatus.DEAD);
                } else {
                    PlayerStatus newStatus = switch (hPlayerEvent.getPlayerEventType()) {
                        case SLIGHT_INJURY -> PlayerStatus.SLIGHT_INJURY;
                        case MODERATE_INJURY -> PlayerStatus.MODERATE_INJURY;
                        case SEVERE_INJURY -> PlayerStatus.SEVERE_INJURY;
                        default -> throw new IllegalStateException("Unexpected value: " + hPlayerEvent.getPlayerEventType());
                    };
                    Player player = hPlayerEvent.getPlayer();
                    player.setStatus(newStatus);
                }
            }
            case PLANNED_EVENT -> {
                HPlannedEvent hPlannedEvent = (HPlannedEvent) happenedEvent;
                PlannedEvent plannedEvent = hPlannedEvent.getPlannedEvent();
                plannedEvent.setStatus(PlannedEventStatus.STARTED);
            }
            case SUPPLY -> {
                HSupplyEvent supplyEvent = (HSupplyEvent) happenedEvent;
                Supply supply = supplyEvent.getSupply();
                supply.setStatus(SupplyStatus.DELIVERED);
            }
        }
    }

    @Transactional
    public PlannedEventDTO createPlannedEvent(Long gameId, PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent = plannedEventMapper.toEntity(dto);

        Game game = Utils.findOrThrow(gameRepository, gameId, "Game");
        EventType eventType = Utils.findOrThrow(eventTypeRepository, dto.getEventTypeId(), "Event type");

        plannedEvent.setGame(game);
        plannedEvent.setEventType(eventType);
        plannedEvent = plannedEventRepository.save(plannedEvent);

        scheduleEvent(plannedEvent);

        return plannedEventMapper.toDto(plannedEvent);
    }

    @Transactional
    public void removePlannedEvent(Long gameId, Long plannedEventId) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");
        Utils.existsOrThrow(plannedEventRepository, plannedEventId, "Planned event");

        plannedEventRepository.deleteById(plannedEventId);

        unscheduleEvent(plannedEventId);
    }

    private void scheduleEvent(PlannedEvent plannedEvent) {
        Instant startTime = plannedEvent.getStartAt().atZone(ZoneId.systemDefault()).toInstant();
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(new StartEventTask(plannedEvent, scheduledEvents), startTime);
        scheduledEvents.put(plannedEvent.getId(), scheduledFuture);
    }

    private void unscheduleEvent(Long plannedEventId) {
        ScheduledFuture<?> scheduledFuture = scheduledEvents.remove(plannedEventId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
