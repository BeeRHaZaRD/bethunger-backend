package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.*;
import com.hg.bethunger.dto.controlsystem.GameStartDTO;
import com.hg.bethunger.dto.controlsystem.HappenedEventCreateDTO;
import com.hg.bethunger.exception.InternalErrorException;
import com.hg.bethunger.exception.ResourceAlreadyExistsException;
import com.hg.bethunger.mapper.GameItemMapper;
import com.hg.bethunger.mapper.GameMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlayerMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.compositekeys.GameItemKey;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.repository.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GameService {
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final GameItemRepository gameItemRepository;
    private final GameItemMapper gameItemMapper;
    private final PlayerMapper playerMapper;
    private final EventService eventService;
    private final WebClient webClient;

    @Autowired
    public GameService(GameRepository gameRepository, GameMapper gameMapper, UserRepository userRepository, PlayerRepository playerRepository, ItemRepository itemRepository, GameItemRepository gameItemRepository, GameItemMapper gameItemMapper, PlayerMapper playerMapper, EventService eventService, WebClient webClient) {
        this.gameRepository = gameRepository;
        this.gameMapper = gameMapper;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
        this.gameItemRepository = gameItemRepository;
        this.gameItemMapper = gameItemMapper;
        this.playerMapper = playerMapper;
        this.eventService = eventService;
        this.webClient = webClient;
    }

    public GameFullDTO getGameById(Long id) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");
        return gameMapper.toFullDto(game);
    }

    public List<GameInfoDTO> getAllGames() {
        return MappingUtils.mapList(
            gameRepository.findAll(), gameMapper::toInfoDto
        );
    }

    public List<GameInfoDTO> getPublicGames() {
        return MappingUtils.mapList(
            gameRepository.findAllByStatusIn(List.of(GameStatus.PLANNED, GameStatus.ONGOING, GameStatus.COMPLETED)), gameMapper::toInfoDto
        );
    }

    @Transactional
    public GameFullDTO createGame(GameCreateDTO gameCreateDTO) {
        Game game = gameMapper.toEntity(gameCreateDTO);

        User manager = Utils.findByIdOrThrow(userRepository, gameCreateDTO.getManagerId(), "User");
        game.setManager(manager);

        return gameMapper.toFullDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public GameFullDTO updateGame(Long id, GameUpdateDTO gameUpdateDTO) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");

        BeanUtils.copyProperties(gameUpdateDTO, game);
        return gameMapper.toFullDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public void publishGame(Long id) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");

        if (game.getStatus() != GameStatus.DRAFT) {
            throw new IllegalStateException("Недопустимый статус игры");
        }
        if (!game.isInfoValid()) {
            throw new IllegalStateException("Информация об игре не заполнена");
        }
        if (!game.isPlayersFull()) {
            throw new IllegalStateException("Не все игроки добавлены");
        }
        game.updateStatus(GameStatus.PLANNED);
        gameRepository.save(game);
    }

    @Transactional
    public void startGame(Long id) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");
        LocalDateTime now = LocalDateTime.now();

        if (game.getStatus() != GameStatus.PLANNED) {
            throw new IllegalStateException("Недопустимый статус игры");
        }
        if (!game.isPlayersTrainResultsFull()) {
            throw new IllegalStateException("Не у всех игроков заполнены результаты тренировок");
        }
        if (game.getDateStart().isAfter(now)) {
            throw new IllegalStateException("Дата начала игры еще не наступила");
        }

        game.setDateStart(now);
        game.updateStatus(GameStatus.ONGOING);
        game = gameRepository.save(game);

        for (PlannedEvent plannedEvent : game.getPlannedEvents()) {
            eventService.scheduleEvent(plannedEvent, now);
        }

        try {
            startGameRequest(game);
            eventService.createHappenedEvent(game.getId(), HappenedEventCreateDTO.createOther("Игра началась"));
        } catch (WebClientResponseException ex) {
            throw new InternalErrorException("Игра не может быть начата из-за отказа системы управления");
        }
    }

    private void startGameRequest(Game game) throws WebClientResponseException {
        List<Long> playersId = game.getPlayers().stream().map(Player::getId).toList();

        webClient.post()
            .uri("/start-game/" + game.getId())
            .bodyValue(new GameStartDTO(playersId))
            .retrieve()
            .toBodilessEntity()
            .block();
    }

    @Transactional
    public PlayerDTO addPlayer(Long gameId, Long playerId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findByIdOrThrow(playerRepository, playerId, "Player");

        game.addPlayer(player);
        gameRepository.save(game);

        return playerMapper.toDto(player);
    }

    @Transactional
    public void removePlayer(Long gameId, Long playerId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findByIdOrThrow(playerRepository, playerId, "Player");

        game.removePlayer(player);
        gameRepository.save(game);
    }

    @Transactional
    public GameItemDTO addItem(Long gameId, Long itemId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Item item = Utils.findByIdOrThrow(itemRepository, itemId, "Item");

        GameItem gameItem = new GameItem(game, item);

        if (gameItemRepository.existsById(gameItem.getId())) {
            throw new ResourceAlreadyExistsException("Предмет уже добавлен в игру");
        }

        return gameItemMapper.toDto(
            gameItemRepository.save(gameItem)
        );
    }

    @Transactional
    public void removeItem(Long gameId, Long itemId) {
        Utils.existsOrThrow(gameRepository, gameId, "Game");
        Utils.existsOrThrow(itemRepository, itemId, "Item");

        GameItemKey gameItemId = new GameItemKey(gameId, itemId);
        gameItemRepository.deleteById(gameItemId);
    }
}
