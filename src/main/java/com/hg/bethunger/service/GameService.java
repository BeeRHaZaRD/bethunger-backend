package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.*;
import com.hg.bethunger.mapper.GameItemMapper;
import com.hg.bethunger.mapper.GameMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.mapper.PlayerMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.compositekeys.GameItemKey;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
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

    public GameFullDTO getGameById(Long id) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");
        return gameMapper.toFullDto(game);
    }

    public List<GameInfoDTO> getGames() {
        return MappingUtils.mapList(
            gameRepository.findAll(), gameMapper::toInfoDto
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

        // TODO validation

        game.updateStatus(GameStatus.PLANNED);
    }

    @Transactional
    public void startGame(Long id) {
        Game game = Utils.findByIdOrThrow(gameRepository, id, "Game");

        // TODO validation

        eventService.scheduleEvents(game.getPlannedEvents());

        game.updateStatus(GameStatus.ONGOING);
    }

    @Transactional
    public PlayerDTO addPlayer(Long gameId, Long playerId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findByIdOrThrow(playerRepository, playerId, "Player");

        game.addPlayer(player);

        return playerMapper.toDto(player);
    }

    @Transactional
    public void removePlayer(Long gameId, Long playerId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findByIdOrThrow(playerRepository, playerId, "Player");

        game.removePlayer(player);
    }

    @Transactional
    public GameItemDTO addItem(Long gameId, Long itemId) {
        Game game = Utils.findByIdOrThrow(gameRepository, gameId, "Game");
        Item item = Utils.findByIdOrThrow(itemRepository, itemId, "Item");

        GameItem gameItem = new GameItem(game, item);

        return gameItemMapper.toDto(
            gameItemRepository.save(gameItem)
        );
    }

    @Transactional
    public void removeItem(Long gameId, Long itemId) {
        GameItemKey gameItemId = new GameItemKey(gameId, itemId);
        gameItemRepository.deleteById(gameItemId);
    }
}
