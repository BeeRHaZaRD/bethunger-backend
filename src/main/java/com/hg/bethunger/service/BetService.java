package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.BetCreateDTO;
import com.hg.bethunger.dto.BetDTO;
import com.hg.bethunger.mapper.BetMapper;
import com.hg.bethunger.mapper.MappingUtils;
import com.hg.bethunger.model.Bet;
import com.hg.bethunger.model.Game;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.User;
import com.hg.bethunger.repository.BetRepository;
import com.hg.bethunger.repository.PlayerRepository;
import com.hg.bethunger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BetService {
    private final BetRepository betRepository;
    private final BetMapper betMapper;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Value("${bethunger.bet_margin}")
    private Double margin;

    @Autowired
    public BetService(BetRepository betRepository, BetMapper betMapper, PlayerRepository playerRepository, UserRepository userRepository) {
        this.betRepository = betRepository;
        this.betMapper = betMapper;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    public List<BetDTO> getBetsByUser(User user) {
        return MappingUtils.mapList(
            betRepository.findAllByUserId(user.getId()), betMapper::toDto
        );
    }

    @Transactional
    public BetDTO createBet(User user, BetCreateDTO dto) {
        Bet bet = betMapper.toEntity(dto);

        User admin = userRepository.findByUsername("admin").orElseThrow();
        Player player = Utils.findByIdOrThrow(playerRepository, dto.getPlayerId(), "Player");

        Game game = player.getGame();
        if (game == null) {
            throw new IllegalStateException("Указанный игрок не относится ни к одной игре");
        }
        if (!game.isPlanned()) {
            throw new IllegalStateException("Совершать ставки можно только в запланированной игре");
        }

        bet.setUser(user);
        bet.setPlayer(player);

        var betAmount = bet.getAmount();
        user.getAccount().subtractMoney(betAmount);
        admin.getAccount().addMoney(betAmount);

        userRepository.save(admin);
        userRepository.save(user);
        betRepository.save(bet);

        playerRepository.updateOddsByGameId(game.getId(), margin);
        player = playerRepository.findById(player.getId()).orElseThrow();
        bet.setPlayer(player);

        return betMapper.toDto(
            betRepository.save(bet)
        );
    }
}
