package com.hg.bethunger;

import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.HPlayerEventType;
import com.hg.bethunger.model.enums.HappenedEventType;
import com.hg.bethunger.model.enums.UserRole;
import com.hg.bethunger.model.init.InitData;
import com.hg.bethunger.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Log4j2
public class RunAfterStartup {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final EventTypeRepository eventTypeRepository;
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final PlannedEventRepository plannedEventRepository;
    private final HappenedEventRepository happenedEventRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initialiseEntities() {
        User adminUser = userRepository.findByUsername("admin").orElseGet(() -> {
            User user = new User();
            user.setRole(UserRole.ADMIN);
            user.setUsername("admin");
            user.setPassword("admin");
            user.setFirstName("System");
            user.setLastName("Admin");
            userRepository.save(user);
            return user;
        });

        User manager01 = userRepository.findByUsername("manager01").orElseGet(() -> {
            User user = new User();
            user.setRole(UserRole.MANAGER);
            user.setUsername("manager01");
            user.setPassword("manager01");
            user.setFirstName("Плутарх");
            user.setLastName("Хэвенсби");
            userRepository.save(user);
            return user;
        });

        if (!itemRepository.existsById(1L)) {
            itemRepository.saveAll(InitData.items);
        }

        Game game74 = gameRepository.findByName("Голодные игры #74").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #74");
            game.setManager(manager01);
            gameRepository.save(game);
            return game;
        });

        Game game75 = gameRepository.findByName("Голодные игры #75").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #75");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.of(2024, 12, 1, 0, 0));
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));
            EventType eventType4 = eventTypeRepository.save(new EventType(game, "Кислотный дождь", "Дождевые осадки повышенной кислотности. При попадании на тело вызывают химические ожоги"));

            InitData.players.forEach(player -> player.setGame(game));
            playerRepository.saveAll(InitData.players);

            PlannedEvent plannedEvent1 = plannedEventRepository.save(new PlannedEvent(eventType1, game, LocalDateTime.of(2024, 12, 1, 1, 0)));
            PlannedEvent plannedEvent2 = plannedEventRepository.save(new PlannedEvent(eventType2, game, LocalDateTime.of(2024, 12, 1, 2, 0)));
            PlannedEvent plannedEvent3 = plannedEventRepository.save(new PlannedEvent(eventType3, game, LocalDateTime.of(2024, 12, 1, 3, 0)));

            happenedEventRepository.save(new HPlannedEvent(game, HappenedEventType.PLANNED_EVENT, LocalDateTime.of(2024, 12, 1, 1, 0), plannedEvent1));
            happenedEventRepository.save(new HPlayerEvent(game, HappenedEventType.PLAYER, LocalDateTime.of(2024, 12, 1, 2, 0), InitData.players.get(0), HPlayerEventType.KILLED));
            happenedEventRepository.save(new HPlayerEvent(game, HappenedEventType.PLAYER, LocalDateTime.of(2024, 12, 1, 3, 0), InitData.players.get(1), HPlayerEventType.SLIGHT_INJURY));
            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, LocalDateTime.of(2024, 12, 1, 4, 0), InitData.players.get(2), "Достиг Рога Изобилия"));

            return game;
        });
        log.debug("DB init is completed");
    }
}
