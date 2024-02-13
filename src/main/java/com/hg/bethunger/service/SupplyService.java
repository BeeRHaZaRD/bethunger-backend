package com.hg.bethunger.service;

import com.hg.bethunger.Utils;
import com.hg.bethunger.dto.SupplyCreateDTO;
import com.hg.bethunger.dto.SupplyDTO;
import com.hg.bethunger.dto.controlsystem.SupplyRequestDTO;
import com.hg.bethunger.exception.ResourceNotFoundException;
import com.hg.bethunger.mapper.SupplyMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.SupplyStatus;
import com.hg.bethunger.repository.ItemRepository;
import com.hg.bethunger.repository.PlayerRepository;
import com.hg.bethunger.repository.SupplyRepository;
import com.hg.bethunger.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@CommonsLog
@Service
@Transactional(readOnly = true)
public class SupplyService {
    private final SupplyRepository supplyRepository;
    private final SupplyMapper supplyMapper;
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;

    @Value("${bethunger.supply_cooldown}")
    private Duration cooldown;

    @Autowired
    public SupplyService(SupplyRepository supplyRepository, SupplyMapper supplyMapper, PlayerRepository playerRepository, ItemRepository itemRepository, UserRepository userRepository, WebClient webClient) {
        this.supplyRepository = supplyRepository;
        this.supplyMapper = supplyMapper;
        this.playerRepository = playerRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.webClient = webClient;
    }

    @Transactional
    public SupplyDTO createSupply(User user, SupplyCreateDTO dto) {
        Supply supply = supplyMapper.toEntity(dto);

        Player player = Utils.findByIdOrThrow(playerRepository, dto.getPlayerId(), "Player");
        Item item = Utils.findByIdOrThrow(itemRepository, dto.getItemId(), "Item");

        Game game = player.getGame();
        if (game == null) {
            throw new IllegalStateException("Указанный игрок не относится ни к одной игре");
        }
        if (!game.isOngoing()) {
            throw new IllegalStateException("Спонсировать игрока можно только в игре, находящейся в процессе");
        }

        GameItem gameItem = game.getGameItem(item.getId()).orElseThrow(
            () -> new ResourceNotFoundException("Указанный предмет отсутствует в данной игре")
        );
        if (!gameItem.isAvailable()) {
            throw new IllegalStateException("Указанный предмет недоступен");
        }
        if (player.getStatus() == PlayerStatus.DEAD) {
            throw new IllegalStateException("Нельзя спонсировать погибшего игрока");
        }
        if (!player.isSuppliable()) {
            throw new IllegalStateException("Спонсирование указанного игрока еще не доступно");
        }

        BigDecimal supplyCost = item.getPrice();
        user.getAccount().subtractMoney(supplyCost);

        gameItem.setAvailable(false);

        supply.setUser(user);
        supply.setPlayer(player);
        supply.setItem(item);
        supply.setAmount(supplyCost);

        player.setCooldownTo(LocalDateTime.now().plus(cooldown));

        userRepository.save(user);
        playerRepository.save(player);
        supply = supplyRepository.save(supply);

        try {
            requestSupply(supply);
            supply.setStatus(SupplyStatus.REQUESTED);
        } catch (WebClientResponseException ex) {
            supply.setStatus(SupplyStatus.CANCELLED);
        }

        return supplyMapper.toDto(
            supplyRepository.save(supply)
        );
    }

    private void requestSupply(Supply supply) {
        var body = new SupplyRequestDTO(
            supply.getId(),
            supply.getPlayer().getId(),
            supply.getItem().getId()
        );
        webClient.post()
            .uri("/events/supply")
            .bodyValue(body)
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}
