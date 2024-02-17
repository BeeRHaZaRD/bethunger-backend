package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.BetCreateDTO;
import com.hg.bethunger.dto.BetDTO;
import com.hg.bethunger.mapper.BetMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.repository.BetRepository;
import com.hg.bethunger.repository.PlayerRepository;
import com.hg.bethunger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BetService {
    private final BetRepository betRepository;
    private final BetMapper betMapper;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    @Value("${bethunger.margin}")
    private Double margin;

    @Autowired
    public BetService(BetRepository betRepository, BetMapper betMapper, PlayerRepository playerRepository, UserRepository userRepository) {
        this.betRepository = betRepository;
        this.betMapper = betMapper;
        this.playerRepository = playerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BetDTO createBet(User user, BetCreateDTO dto) {
        Bet bet = betMapper.toEntity(dto);
        Player player = Utils.findByIdOrThrow(playerRepository, dto.getPlayerId(), "Player");
        User admin = userRepository.findByUsername("admin").orElseThrow();

        Game game = player.getGame();
        if (game == null) {
            throw new IllegalStateException("Указанный игрок не относится ни к одной игре");
        }
        if (!game.isPlanned()) {
            throw new IllegalStateException("Делать ставки можно только в запланированных играх");
        }

        bet.setUser(user);
        bet.setPlayer(player);
        admin.getAccount().addMoney(bet.getAmount());
        user.getAccount().subtractMoney(bet.getAmount());

        userRepository.save(admin);
        userRepository.save(user);
        betRepository.save(bet);
        playerRepository.updateCoefficientsByGameId(game.getId(), margin);

        return betMapper.toDto(betRepository.save(bet));
    }
}
