package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.HappenedEventDTO;
import com.hg.bethunger.dto.PlannedEventCreateDTO;
import com.hg.bethunger.dto.controlsystem.HappenedEventCreateDTO;
import com.hg.bethunger.dto.controlsystem.PlannedEventRequestDTO;
import com.hg.bethunger.mapper.HappenedEventMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlannedEventMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.*;
import com.hg.bethunger.repository.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Stream;

@CommonsLog
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
    private final BetRepository betRepository;
    private final UserRepository userRepository;
    private final TaskScheduler taskScheduler;
    private final WebClient webClient;

    private final Map<Long, ScheduledFuture<?>> scheduledEvents = new ConcurrentHashMap<>();

    @Value("${bethunger.bet_margin}")
    private Double margin;

    @Autowired
    public EventService(PlannedEventRepository plannedEventRepository, PlannedEventMapper plannedEventMapper, HappenedEventRepository<HappenedEvent> happenedEventRepository, HPlayerEventRepository hPlayerEventRepository, HOtherEventRepository hOtherEventRepository, HappenedEventMapper happenedEventMapper, GameRepository gameRepository, PlayerRepository playerRepository, SupplyRepository supplyRepository, EventTypeRepository eventTypeRepository, TaskScheduler taskScheduler, WebClient webClient, BetRepository betRepository, UserRepository userRepository) {
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
        this.betRepository = betRepository;
        this.userRepository = userRepository;
        this.taskScheduler = taskScheduler;
        this.webClient = webClient;
    }

    public List<HappenedEventDTO> getHappenedEvents(Long gameId) {
        Utils.existsByIdOrThrow(gameRepository, gameId, "Game");

        return MappingUtils.mapList(
            happenedEventRepository.findAllByGameIdOrderByHappenedAtDesc(gameId), happenedEventMapper::toDTO
        );
    }

    public List<HappenedEventDTO> getHappenedEventsAfter(Long gameId, LocalDateTime after) {
        Utils.existsByIdOrThrow(gameRepository, gameId, "Game");

        return MappingUtils.mapList(
            happenedEventRepository.findAllByGameIdAndHappenedAtAfterOrderByHappenedAtDesc(gameId, after), happenedEventMapper::toDTO
        );
    }

    public List<HappenedEventDTO> getHappenedEventsByPlayer(Long gameId, Long playerId) {
        Utils.existsByIdOrThrow(gameRepository, gameId, "Game");
        Utils.existsByIdOrThrow(playerRepository, playerId, "Player");

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
                    betRepository.updateStatusByPlayerId(player.getId(), BetStatus.LOSS);
                    log.debug("Happened event: Player killed");

                    // game completed
                    Game game = happenedEvent.getGame();
                    if (game.playersLeft() == 1) {
                        finishGameHandler(game, happenedEvent.getHappenedAt());
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
                    log.debug("Happened event: Player injured");
                }
            }
            case PLANNED_EVENT -> {
                HPlannedEvent hPlannedEvent = (HPlannedEvent) happenedEvent;
                PlannedEvent plannedEvent = hPlannedEvent.getPlannedEvent();
                plannedEvent.setStatus(PlannedEventStatus.STARTED);
                log.debug("Happened event: Planned event");
            }
            case SUPPLY -> {
                HSupplyEvent supplyEvent = (HSupplyEvent) happenedEvent;
                Supply supply = supplyEvent.getSupply();
                supply.setStatus(SupplyStatus.DELIVERED);
                log.debug("Happened event: Supply");
            }
            case OTHER -> {
                log.debug("Happened event: Other");
            }
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void finishGameHandler(Game game, LocalDateTime finishDate) {
        game.setWinnerOnComplete();
        game.setDuration(Duration.between(game.getDateStart(), finishDate));
        game.updateStatus(GameStatus.COMPLETED);

        // change bets status and send winnings
        List<Bet> winBets = betRepository.findAllByPlayerId(game.getWinner().getId());
        if (winBets.isEmpty()) {
            userRepository.returnFundsToUsers(game.getId(), margin);
        } else {
            betRepository.updateStatusByPlayerId(game.getWinner().getId(), BetStatus.WIN);
            userRepository.addWinningsToUsers(game.getWinner().getId());
        }
        userRepository.subtractMoneyFromAdmin(game.getId(), margin);

        // cancel all the rest planned events, both SCHEDULED and REQUESTED
        game.getPlannedEvents().stream()
            .filter(plannedEvent -> plannedEvent.getStatus() == PlannedEventStatus.SCHEDULED || plannedEvent.getStatus() == PlannedEventStatus.REQUESTED)
            .forEach(plannedEvent -> {
                plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
                unscheduleEvent(plannedEvent.getId());
            });

        happenedEventRepository.save(new HOtherEvent(game, finishDate.plusSeconds(1), "Игра завершена"));
        log.debug("Game %d is finished".formatted(game.getId()));
    }

    @Transactional
    public PlannedEvent createPlannedEvent(Long gameId, PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent = plannedEventMapper.toEntity(dto);

        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        EventType eventType = Utils.findByIdOrThrow(eventTypeRepository, dto.getEventTypeId(), "Event type");

        if (game.isCompleted()) {
            throw new IllegalStateException("Нелья запланировать событие для завершенной игры");
        }
        if (!eventType.getGame().getId().equals(game.getId())) {
            throw new IllegalStateException("Указанный тип события не относится к данной игре");
        }
        if (game.getPlannedEvents().stream().anyMatch(event ->
            event.getStartAt().equals(plannedEvent.getStartAt()))) {
            throw new IllegalStateException("На указанное время уже запланировано другое событие");
        }
        if (plannedEvent.getStartAt().isBefore(game.getDateStart())) {
            throw new IllegalStateException("Дата запуска планируемого события не может быть меньше даты начала игры");
        }

        plannedEvent.setGame(game);
        plannedEvent.setEventType(eventType);

        return plannedEventRepository.save(plannedEvent);
    }

    @Transactional
    public PlannedEvent runPlannedEvent(Long gameId, PlannedEventCreateDTO dto) {
        PlannedEvent plannedEvent = createPlannedEvent(gameId, dto);

        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        if (!game.isOngoing()) {
            throw new IllegalStateException("Событие можно запустить только для игры, находящейся в процессе");
        }

        try {
            requestPlannedEvent(plannedEvent);
            plannedEvent.setStatus(PlannedEventStatus.REQUESTED);
        } catch (WebClientResponseException ex) {
            plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
        }
        return plannedEventRepository.save(plannedEvent);
    }

    @Transactional
    public void removePlannedEvent(Long gameId, Long plannedEventId) {
        Utils.existsByIdOrThrow(gameRepository, gameId, "Game");
        Utils.existsByIdOrThrow(plannedEventRepository, plannedEventId, "Planned event");

        plannedEventRepository.deleteById(plannedEventId);
    }

    @Transactional
    public void scheduleEvent(PlannedEvent plannedEvent, LocalDateTime gameDateStart) {
        if (plannedEvent.getStartAt().isBefore(gameDateStart)) {
            plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
            plannedEventRepository.save(plannedEvent);
            return;
        }

        Runnable requestPlannedEventTask = () -> {
            try {
                requestPlannedEvent(plannedEvent);
                plannedEvent.setStatus(PlannedEventStatus.REQUESTED);
            } catch (WebClientResponseException ex) {
                plannedEvent.setStatus(PlannedEventStatus.CANCELLED);
            }
            plannedEventRepository.save(plannedEvent);
            scheduledEvents.remove(plannedEvent.getId());
        };

        Instant startTime = plannedEvent.getStartAt().atZone(ZoneId.systemDefault()).toInstant();
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(requestPlannedEventTask, startTime);
        scheduledEvents.put(plannedEvent.getId(), scheduledFuture);
    }

    private void requestPlannedEvent(PlannedEvent plannedEvent) throws WebClientResponseException {
        var body = new PlannedEventRequestDTO(plannedEvent.getId(), plannedEvent.getEventType().getId());
        webClient.post()
            .uri("/events/planned-event")
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    private void unscheduleEvent(Long plannedEventId) {
        ScheduledFuture<?> scheduledFuture = scheduledEvents.remove(plannedEventId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
