package com.hg.bethunger;

import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.mapper.UserMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.*;
import com.hg.bethunger.model.init.InitData;
import com.hg.bethunger.repository.*;
import com.hg.bethunger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@CommonsLog
public class RunAfterStartup {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final EventTypeRepository eventTypeRepository;
    private final PlayerRepository playerRepository;
    private final ItemRepository itemRepository;
    private final PlannedEventRepository plannedEventRepository;
    private final HappenedEventRepository<HappenedEvent> happenedEventRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void initialiseDb() {
        User adminUser = userRepository.findByUsername("admin").orElseGet(() -> {
            UserCreateDTO userCreateDTO = new UserCreateDTO("admin", "admin", "System", "Admin");
            UserDTO userDTO = userService.createUser(userCreateDTO, UserRole.ADMIN);
            return userMapper.toEntity(userDTO);
        });

        User manager01 = userRepository.findByUsername("manager01").orElseGet(() -> {
            UserCreateDTO userCreateDTO = new UserCreateDTO("manager01", "manager01", "Плутарх", "Хэвенсби");
            UserDTO userDTO = userService.createUser(userCreateDTO, UserRole.MANAGER);
            return userMapper.toEntity(userDTO);
        });

        User manager02 = userRepository.findByUsername("manager02").orElseGet(() -> {
            UserCreateDTO userCreateDTO = new UserCreateDTO("manager02", "manager02", "Сенека", "Крэйн");
            UserDTO userDTO = userService.createUser(userCreateDTO, UserRole.MANAGER);
            return userMapper.toEntity(userDTO);
        });

        User user01 = userRepository.findByUsername("user01").orElseGet(() -> {
            UserCreateDTO userCreateDTO = new UserCreateDTO("user01", "user01", "Иван", "Иванов");
            UserDTO userDTO = userService.createUser(userCreateDTO, UserRole.USER);
            return userMapper.toEntity(userDTO);
        });

        if (itemRepository.count() == 0) {
            itemRepository.saveAll(InitData.items);
        }

        List<Player> availablePlayers = InitData.playersFullAlive.stream().map(Player::clone).toList();
        playerRepository.saveAll(availablePlayers);

        List<Player> players74 = InitData.playersNotFull.stream().map(Player::clone).toList();
        List<Player> players75 = InitData.playersFullAlive.stream().map(Player::clone).toList();
        List<Player> players76 = InitData.playersFullAlive.stream().map(Player::clone).toList();
        List<Player> players77 = InitData.playersFullRandom.stream().map(Player::clone).toList();
        List<Player> players78 = InitData.playersOneAlive.stream().map(Player::clone).toList();

        // DRAFT пустая
        Game game73 = gameRepository.findByName("Голодные игры #73").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #73");
            game.setManager(manager01);
            gameRepository.save(game);
            return game;
        });

        // DRAFT не готова к публикации [инфо частично / предметы нет / ЗС нет / игроки частично]
        Game game74 = gameRepository.findByName("Голодные игры #74").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #74");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.of(2024, 12, 1, 12, 0));
            game.setArenaType("Джунгли");
            gameRepository.save(game);

            players74.forEach(player -> player.setGame(game));
            playerRepository.saveAll(players74);

            return game;
        });

        // DRAFT готова к публикации [инфо вся / предметы нет / ЗС нет / игроки все]
        Game game75 = gameRepository.findByName("Голодные игры #75").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #75");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.of(2024, 11, 1, 12, 0));
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players75.forEach(player -> player.setGame(game));
            playerRepository.saveAll(players75);

            return game;
        });

        // TODO PLANNED не готова к запуску [предметы нет / ЗС нет / тренировки частично]

        // PLANNED готова к запуску [предметы нет / ЗС есть / тренировки все]
        Game game76 = gameRepository.findByName("Голодные игры #76").orElseGet(() -> {
            Game game = new Game();
            game.setStatus(GameStatus.PLANNED);
            game.setName("Голодные игры #76");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.now().plusMinutes(5));
            game.setDateStart(LocalDateTime.now());
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players76.forEach(player -> {
                player.setGame(game);
                player.setTrainResults(new TrainResults());
            });
            playerRepository.saveAll(players76);

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));
            EventType eventType4 = eventTypeRepository.save(new EventType(game, "Кислотный дождь", "Дождевые осадки повышенной кислотности. При попадании на тело вызывают химические ожоги"));

            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType1, game, game.getDateStart().plusMinutes(5)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType2, game, game.getDateStart().plusMinutes(10)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType3, game, game.getDateStart().plusHours(1)));

            return game;
        });

        // ONGOING [предметы нет / ЗС есть / ПС есть]
        Game game77 = gameRepository.findByName("Голодные игры #77").orElseGet(() -> {
            Game game = new Game();
            game.setStatus(GameStatus.ONGOING);
            game.setName("Голодные игры #77");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.of(2024, 1, 27, 12, 0));
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players77.forEach(player -> player.setGame(game));
            playerRepository.saveAll(players77);

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));

            PlannedEvent plannedEvent1 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType1, game, game.getDateStart().plusHours(1)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType3, game, game.getDateStart().plusHours(2)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType2, game, LocalDateTime.now().plusHours(1)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType3, game, LocalDateTime.now().plusHours(2)));

            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, game.getDateStart(), "Игра началась"));
            happenedEventRepository.save(new HPlannedEvent(game, HappenedEventType.PLANNED_EVENT, plannedEvent1.getStartAt(), plannedEvent1));
            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, game.getDateStart().plusMinutes(10), players77.get(1), "Достиг Рога Изобилия"));
            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, game.getDateStart().plusMinutes(20), players77.get(1), "Достиг переправы"));
            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, game.getDateStart().plusMinutes(30), "Распорядитель выступил с объявлением"));
            happenedEventRepository.save(new HPlayerEvent(game, HappenedEventType.PLAYER, game.getDateStart().plusHours(1), players77.get(0), HPlayerEventType.KILLED));
            happenedEventRepository.save(new HPlayerEvent(game, HappenedEventType.PLAYER, game.getDateStart().plusHours(2), players77.get(1), HPlayerEventType.SLIGHT_INJURY));

            return game;
        });

        // COMPLETED
        Game game78 = gameRepository.findByName("Голодные игры #78").orElseGet(() -> {
            Game game = new Game();
            game.setStatus(GameStatus.COMPLETED);
            game.setName("Голодные игры #78");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.of(2024, 1, 20, 12, 0));
            game.setDuration(Duration.between(game.getDateStart(), LocalDateTime.of(2024, 1, 25, 20, 36)));
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players78.forEach(player -> player.setGame(game));
            playerRepository.saveAll(players78);

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));

            PlannedEvent plannedEvent1 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType1, game, game.getDateStart().plusHours(5)));
            PlannedEvent plannedEvent2 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType2, game, game.getDateStart().plusHours(6)));
            PlannedEvent plannedEvent3 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType3, game, game.getDateStart().plusHours(7)));

            List<HPlayerEvent> playerEvents = IntStream.range(0, 24)
                .filter(i -> i != 5)
                .mapToObj(i -> new HPlayerEvent(game, HappenedEventType.PLAYER, game.getDateStart().plusHours(i+1), players78.get(i), HPlayerEventType.KILLED))
                .toList();
            happenedEventRepository.saveAll(playerEvents);
            happenedEventRepository.save(new HOtherEvent(game, HappenedEventType.OTHER, game.getDateStart(), "Игра началась"));
            happenedEventRepository.save(new HPlannedEvent(game, HappenedEventType.PLANNED_EVENT, plannedEvent1.getStartAt(), plannedEvent1));
            happenedEventRepository.save(new HPlannedEvent(game, HappenedEventType.PLANNED_EVENT, plannedEvent2.getStartAt(), plannedEvent2));
            happenedEventRepository.save(new HPlannedEvent(game, HappenedEventType.PLANNED_EVENT, plannedEvent3.getStartAt(), plannedEvent3));

            return game;
        });
        game78.setWinner(players78.get(5));
        gameRepository.save(game78);

        log.debug("DB init is completed");
    }
}
