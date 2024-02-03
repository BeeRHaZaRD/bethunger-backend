package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.HappenedEventCreateDTO;
import com.hg.bethunger.dto.HappenedEventDTO;
import com.hg.bethunger.dto.PlannedEventCreateDTO;
import com.hg.bethunger.dto.PlannedEventDTO;
import com.hg.bethunger.mapper.HappenedEventMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlannedEventMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.*;
import com.hg.bethunger.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class EventService {
    private final PlannedEventRepository plannedEventRepository;
    private final PlannedEventMapper plannedEventMapper;
    private final HappenedEventRepository<HappenedEvent> happenedEventRepository;
    private final HPlayerEventRepository hPlayerEventRepository;
    private final HOtherEventRepository hOtherEventRepository;
    private final HappenedEventMapper happenedEventMapper;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final SupplyRepository supplyRepository;
    private final EventTypeRepository eventTypeRepository;
    private final TaskScheduler taskScheduler;

    private final Map<Long, ScheduledFuture<?>> scheduledEvents = new HashMap<>();

    @Autowired
    public EventService(PlannedEventRepository plannedEventRepository, PlannedEventMapper plannedEventMapper, HappenedEventRepository<HappenedEvent> happenedEventRepository, HPlayerEventRepository hPlayerEventRepository, HOtherEventRepository hOtherEventRepository, HappenedEventMapper happenedEventMapper, GameRepository gameRepository, PlayerRepository playerRepository, SupplyRepository supplyRepository, EventTypeRepository eventTypeRepository, TaskScheduler taskScheduler) {
        this.plannedEventRepository = plannedEventRepository;
        this.plannedEventMapper = plannedEventMapper;
        this.happenedEventRepository = happenedEventRepository;
        this.hPlayerEventRepository = hPlayerEventRepository;
        this.hOtherEventRepository = hOtherEventRepository;
        this.happenedEventMapper = happenedEventMapper;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.supplyRepository = supplyRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.taskScheduler = taskScheduler;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    @Transactional
    public void scheduleOngoingGameEvents() {
        LocalDateTime now = LocalDateTime.now();
        List<Game> ongoingGames = gameRepository.findAllByStatus(GameStatus.ONGOING);
        for (Game game : ongoingGames) {
            for (PlannedEvent plannedEvent : game.getPlannedEvents()) {
                if (plannedEvent.getStatus() != PlannedEventStatus.SCHEDULED) {
                    continue;
                }
                if (plannedEvent.getStartAt().isAfter(now)) {
                    scheduleEvent(plannedEvent);
                } else {
                    plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
                    plannedEventRepository.save(plannedEvent);
                }
            }
        }
    }

    public List<HappenedEventDTO> getHappenedEvents(Long gameId) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");

        return MappingUtils.mapList(
            happenedEventRepository.findAllByGameIdOrderByHappenedAtDesc(gameId), happenedEventMapper::toDTO
        );
    }

    public List<HappenedEventDTO> getHappenedEventsAfter(Long gameId, LocalDateTime after) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");

        return MappingUtils.mapList(
            happenedEventRepository.findAllByGameIdAndHappenedAtAfterOrderByHappenedAtDesc(gameId, after), happenedEventMapper::toDTO
        );
    }

    public List<HappenedEventDTO> getHappenedEventsByPlayer(Long gameId, Long playerId) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");
        Utils.existsOrThrow(playerRepository, playerId, "Player");

        List<HappenedEvent> happenedPlayerEvents = hPlayerEventRepository.findAllByGameIdAndPlayerIdOrderByHappenedAtDesc(gameId, playerId);
        List<HappenedEvent> happenedOtherEvents = hOtherEventRepository.findAllByGameIdAndPlayerIdOrderByHappenedAtDesc(gameId, playerId);

        return MappingUtils.mapList(
            Stream.of(happenedPlayerEvents, happenedOtherEvents)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(HappenedEvent::getHappenedAt).reversed())
                .toList(),
            happenedEventMapper::toDTO
        );
    }

    @Transactional
    public void createHappenedEvent(Long gameId, HappenedEventCreateDTO dto) {
        HappenedEvent happenedEvent = happenedEventMapper.toEntity(dto);

        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        happenedEvent.setGame(game);

        switch (happenedEvent.getType()) {
            case PLAYER -> {
                HPlayerEvent hPlayerEvent = (HPlayerEvent) happenedEvent;
                Player player = Utils.findByIdOrThrow(playerRepository, dto.getPlayerId(), "Player");
                hPlayerEvent.setPlayer(player);
            }
            case PLANNED_EVENT -> {
                HPlannedEvent hPlannedEvent = (HPlannedEvent) happenedEvent;
                PlannedEvent plannedEvent = Utils.findByIdOrThrow(plannedEventRepository, dto.getPlannedEventId(), "Planned event");
                hPlannedEvent.setPlannedEvent(plannedEvent);
            }
            case SUPPLY -> {
                HSupplyEvent supplyEvent = (HSupplyEvent) happenedEvent;
                Supply supply = Utils.findByIdOrThrow(supplyRepository, dto.getSupplyId(), "Supply");
                supplyEvent.setSupply(supply);
            }
            case OTHER -> {
                HOtherEvent hOtherEvent = (HOtherEvent) happenedEvent;
                if (dto.getPlayerId() != null) {
                    Player player = Utils.findByIdOrThrow(playerRepository, dto.getPlayerId(), "Player");
                    hOtherEvent.setPlayer(player);
                }
            }
        }
        happenedEvent = happenedEventRepository.save(happenedEvent);
        handleHappenedEvent(happenedEvent);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void handleHappenedEvent(HappenedEvent happenedEvent) {
        switch (happenedEvent.getType()) {
            case PLAYER -> {
                HPlayerEvent hPlayerEvent = (HPlayerEvent) happenedEvent;
                if (hPlayerEvent.getPlayerEventType() == HPlayerEventType.KILLED) {
                    Player player = hPlayerEvent.getPlayer();
                    player.updateStatus(PlayerStatus.DEAD);

                    // game completed
                    Game game = happenedEvent.getGame();
                    if (game.playersLeft() == 1) {
                        game.setWinnerOnComplete();
                        game.setDuration(Duration.between(game.getDateStart(), happenedEvent.getHappenedAt()));
                        game.updateStatus(GameStatus.COMPLETED);

                        game.getPlannedEvents().stream()
                            .filter(plannedEvent -> plannedEvent.getStatus() == PlannedEventStatus.SCHEDULED || plannedEvent.getStatus() == PlannedEventStatus.REQUESTED)
                            .forEach(plannedEvent -> {
                                plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
                                unscheduleEvent(plannedEvent.getId());
                            });

                        happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, happenedEvent.getHappenedAt().plusSeconds(1), "Игра завершена"));
                    }
                } else {
                    PlayerStatus newStatus = switch (hPlayerEvent.getPlayerEventType()) {
                        case SLIGHT_INJURY -> PlayerStatus.SLIGHT_INJURY;
                        case MODERATE_INJURY -> PlayerStatus.MODERATE_INJURY;
                        case SEVERE_INJURY -> PlayerStatus.SEVERE_INJURY;
                        default -> throw new IllegalStateException("Unexpected value: " + hPlayerEvent.getPlayerEventType());
                    };
                    Player player = hPlayerEvent.getPlayer();
                    player.updateStatus(newStatus);
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

        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        EventType eventType = Utils.findByIdOrThrow(eventTypeRepository, dto.getEventTypeId(), "Event type");

        if (plannedEvent.getStartAt().isBefore(game.getDateStart())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата запуска планируемого события не может быть меньше даты начала игры");
        }

        plannedEvent.setGame(game);
        plannedEvent.setEventType(eventType);
        plannedEvent = plannedEventRepository.save(plannedEvent);

        return plannedEventMapper.toDto(plannedEvent);
    }

    @Transactional
    public PlannedEventDTO runPlannedEvent(Long gameId, PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent = plannedEventMapper.toEntity(dto);

        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        EventType eventType = Utils.findByIdOrThrow(eventTypeRepository, dto.getEventTypeId(), "Event type");

        plannedEvent.setGame(game);
        plannedEvent.setEventType(eventType);
        plannedEvent = plannedEventRepository.save(plannedEvent);

        // TODO request
        plannedEvent.setStatus(PlannedEventStatus.REQUESTED);
        System.out.println("Start event: " + plannedEvent.getEventType().getName() + " at " + LocalDateTime.now());

        return plannedEventMapper.toDto(plannedEvent);
    }

    @Transactional
    public void removePlannedEvent(Long gameId, Long plannedEventId) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");
        Utils.existsOrThrow(plannedEventRepository, plannedEventId, "Planned event");

        plannedEventRepository.deleteById(plannedEventId);
    }

    @Transactional
    public void scheduleEvents(List<PlannedEvent> plannedEvents, LocalDateTime gameDateStart) {
        for (PlannedEvent plannedEvent : plannedEvents) {
            if (plannedEvent.getStartAt().isAfter(gameDateStart)) {
                scheduleEvent(plannedEvent);
            } else {
                plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
                plannedEventRepository.save(plannedEvent);
            }
        }
    }

    // for usage in scheduleEvents() only
    @Transactional
    public void scheduleEvent(PlannedEvent plannedEvent) {
        Instant startTime = plannedEvent.getStartAt().atZone(ZoneId.systemDefault()).toInstant();

        Runnable requestEventTask = () -> {
            // TODO request
            plannedEvent.setStatus(PlannedEventStatus.REQUESTED);

            System.out.println("Start event: " + plannedEvent.getEventType().getName() + " at " + LocalDateTime.now());
            // if request is successful
            scheduledEvents.remove(plannedEvent.getId());
            plannedEventRepository.save(plannedEvent);
        };
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(requestEventTask, startTime);
        scheduledEvents.put(plannedEvent.getId(), scheduledFuture);
    }

    private void unscheduleEvent(Long plannedEventId) {
        ScheduledFuture<?> scheduledFuture = scheduledEvents.remove(plannedEventId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
