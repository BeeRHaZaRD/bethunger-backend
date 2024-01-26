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

    public GameFullDTO getGameById(Long id) {
        Game game = Utils.findOrThrow(gameRepository, id, "Game");
        return gameMapper.toFullDto(game);
    }

    public List<GameInfoDTO> getGames() {
        return MappingUtils.mapList(
            gameRepository.findAll(), gameMapper::toInfoDto
        );
    }

    @Transactional
    public GameInfoDTO createGame(GameCreateDTO gameCreateDTO) {
        Game game = gameMapper.toEntity(gameCreateDTO);

        User manager = Utils.findOrThrow(userRepository, gameCreateDTO.getManagerId(), "User");
        game.setManager(manager);

        return gameMapper.toInfoDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public GameFullDTO updateGame(Long id, GameUpdateDTO gameUpdateDTO) {
        Game game = Utils.findOrThrow(gameRepository, id, "Game");

        BeanUtils.copyProperties(gameUpdateDTO, game);
        return gameMapper.toFullDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public GameFullDTO publishGame(Long id) {
        Game game = Utils.findOrThrow(gameRepository, id, "Game");

        game.setStatus(GameStatus.PLANNED);
        return gameMapper.toFullDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public GameFullDTO startGame(Long id) {
        Game game = Utils.findOrThrow(gameRepository, id, "Game");

        game.setStatus(GameStatus.ONGOING);
        return gameMapper.toFullDto(
            gameRepository.save(game)
        );
    }

    @Transactional
    public PlayerDTO addPlayer(Long gameId, Long playerId) {
        Game game = Utils.findOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findOrThrow(playerRepository, playerId, "Player");

        game.addPlayer(player);

        return playerMapper.toDto(player);
    }

    @Transactional
    public void removePlayer(Long gameId, Long playerId) {
        Game game = Utils.findOrThrow(gameRepository, gameId, "Game");
        Player player = Utils.findOrThrow(playerRepository, playerId, "Player");

        game.removePlayer(player);
    }

    @Transactional
    public GameItemDTO addItem(Long gameId, Long itemId) {
        Game game = Utils.findOrThrow(gameRepository, gameId, "Game");
        Item item = Utils.findOrThrow(itemRepository, itemId, "Item");

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
