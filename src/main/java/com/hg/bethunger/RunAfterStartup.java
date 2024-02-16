package com.hg.bethunger;

import com.hg.bethunger.dto.UserCreateDTO;
import com.hg.bethunger.dto.UserDTO;
import com.hg.bethunger.mapper.UserMapper;
import com.hg.bethunger.model.*;
import com.hg.bethunger.model.enums.GameStatus;
import com.hg.bethunger.model.enums.HPlayerEventType;
import com.hg.bethunger.model.enums.PlannedEventStatus;
import com.hg.bethunger.model.enums.UserRole;
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
    private final GameItemRepository gameItemRepository;
    private final UserService userService;
    private final UserMapper userMapper;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void initialiseDb() {
        userRepository.findByUsername("cs").orElseGet(() -> {
            UserCreateDTO userCreateDTO = new UserCreateDTO("cs", "h&1jDP5e@lF2rK#b4", "Control", "System");
            UserDTO userDTO = userService.createUser(userCreateDTO, UserRole.ADMIN);
            return userMapper.toEntity(userDTO);
        });

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

        List<Item> items = InitData.items;
        if (itemRepository.count() == 0) {
            itemRepository.saveAll(items);
        }

        List<Player> availablePlayers = InitData.playersFullAlive.stream().map(Player::clone).toList();
        playerRepository.saveAll(availablePlayers);

        // DRAFT пустая
        Game game73 = gameRepository.findByName("Голодные игры #73").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #73");
            game.setManager(manager02);
            gameRepository.save(game);
            return game;
        });

        // DRAFT не готова к публикации [инфо частично / предметы нет / ЗС нет / игроки частично]
        List<Player> players74 = InitData.playersNotFull.stream().map(Player::clone).toList();
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
        List<Player> players75 = InitData.playersFullAlive.stream().map(Player::clone).toList();
        Game game75 = gameRepository.findByName("Голодные игры #75").orElseGet(() -> {
            Game game = new Game();
            game.setName("Голодные игры #75");
            game.setManager(manager02);
            game.setDateStart(LocalDateTime.of(2024, 11, 1, 12, 0));
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players75.forEach(player -> player.setGame(game));
            playerRepository.saveAll(players75);

            return game;
        });

        // PLANNED готова к запуску [предметы есть / ЗС есть / тренировки все]
        List<Player> players77 = InitData.playersFullAlive.stream().map(Player::clone).toList();
        Game game77 = gameRepository.findByName("Голодные игры #77").orElseGet(() -> {
            Game game = new Game();
            game.setStatus(GameStatus.PLANNED);
            game.setName("Голодные игры #77");
            game.setManager(manager01);
            game.setDateStart(LocalDateTime.now());
            game.setDescription("Квартальная бойня. Все участники являются победителями прошлых игр. Единственный источник воды - стволы деревьев, растущие в лесу.");
            game.setArenaType("Джунгли");
            game.setArenaDescription("Состоит из 12 секторов, в каждом из которых по очереди активируется определенное опасное явление. Рог Изобилия находится посередине и представляет собой остров, окруженный соленой водой.");
            gameRepository.save(game);

            players77.forEach(player -> {
                player.setGame(game);
                player.setTrainResults(new TrainResults(1,2,3,4,5,6,7,8));
            });
            playerRepository.saveAll(players77);

            gameItemRepository.save(new GameItem(game, items.get(0)));
            gameItemRepository.save(new GameItem(game, items.get(1)));

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));
            EventType eventType4 = eventTypeRepository.save(new EventType(game, "Кислотный дождь", "Дождевые осадки повышенной кислотности. При попадании на тело вызывают химические ожоги"));

            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType1, game, game.getDateStart().plusMinutes(1)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType2, game, game.getDateStart().plusMinutes(3)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType3, game, game.getDateStart().plusMinutes(10)));
            plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.SCHEDULED, eventType4, game, game.getDateStart().plusMinutes(60)));

            return game;
        });

        // COMPLETED
        List<Player> players78 = InitData.playersOneAlive.stream().map(Player::clone).toList();
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

            players78.forEach(player -> {
                player.setGame(game);
                player.setTrainResults(new TrainResults());
            });
            playerRepository.saveAll(players78);

            gameItemRepository.save(new GameItem(game, items.get(0)));
            gameItemRepository.save(new GameItem(game, items.get(1)));

            EventType eventType1 = eventTypeRepository.save(new EventType(game, "Метеоритный дождь", "Множественное падение каменных обломков в случайных точках арены"));
            EventType eventType2 = eventTypeRepository.save(new EventType(game, "Цунами", "Сильное наводнение, приводящее к затоплению значительной части арены"));
            EventType eventType3 = eventTypeRepository.save(new EventType(game, "Нашествие обезьян", "Смотрели фильм \"Восстание планеты обезьян\"? :)"));

            PlannedEvent plannedEvent1 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType1, game, game.getDateStart().plusHours(5)));
            PlannedEvent plannedEvent2 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType2, game, game.getDateStart().plusHours(6)));
            PlannedEvent plannedEvent3 = plannedEventRepository.save(new PlannedEvent(PlannedEventStatus.STARTED, eventType3, game, game.getDateStart().plusHours(7)));

            happenedEventRepository.save(new HOtherEvent(game, game.getDateStart(), "Игра началась"));
            happenedEventRepository.save(new HPlannedEvent(game, plannedEvent1.getStartAt(), plannedEvent1));
            happenedEventRepository.save(new HPlannedEvent(game, plannedEvent2.getStartAt(), plannedEvent2));
            happenedEventRepository.save(new HPlannedEvent(game, plannedEvent3.getStartAt(), plannedEvent3));
            happenedEventRepository.save(new HOtherEvent(game, game.getDateStart().plusMinutes(5), players78.get(0), "Достиг рога Изобилия"));
            List<HPlayerEvent> playerEvents = IntStream.range(0, 24)
                .filter(i -> i != 5)
                .mapToObj(i -> new HPlayerEvent(game, game.getDateStart().plusHours(i+1), players78.get(i), HPlayerEventType.KILLED))
                .toList();
            happenedEventRepository.saveAll(playerEvents);

            return game;
        });
        game78.setWinner(players78.get(5));
        gameRepository.save(game78);

        log.debug("DB init is finished");
    }
}
