package com.hg.bethunger.model.init;

import com.hg.bethunger.model.Item;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.PlayerStatus;
import com.hg.bethunger.model.enums.Sex;

import java.math.BigDecimal;
import java.util.List;

public class InitData {
    public final static List<Player> playersNotFull = List.of(
        new Player(PlayerStatus.ALIVE, "Блеск", "Ритчсон", 1, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Кашмира", "Ритчсон", 1, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Энобария", "Голдинг", 2, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Бити", "Литье", 3, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Финник", "Одэйр", 4, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Мэгз", "Флэнаган", 4, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джеймс", "Логан", 5, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Иветта", "Ли-Санчес", 5, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джастин", "Хикс", 7, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Джон", "Касино", 8, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Цецилия", "Санчез", 8, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Даниэль", "Бернхардт", 9, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Тиффани", "Вакслер", 10, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Сидер", "Хоуэлл", 11, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Пит", "Мелларк", 12, Sex.MALE)
    );

    public final static List<Player> playersFullAlive = List.of(
        new Player(PlayerStatus.ALIVE, "Блеск", "Ритчсон", 1, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Кашмира", "Ритчсон", 1, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Брут", "Ганн", 2, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Энобария", "Голдинг", 2, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Бити", "Литье", 3, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Вайресс", "Кларк", 3, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Финник", "Одэйр", 4, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Мэгз", "Флэнаган", 4, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джеймс", "Логан", 5, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Иветта", "Ли-Санчес", 5, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Бобби", "Джордан", 6, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Дайме", "Кёсслер", 6, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джастин", "Хикс", 7, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Джоанна", "Мейсон", 7, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джон", "Касино", 8, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Цецилия", "Санчез", 8, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Даниэль", "Бернхардт", 9, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Мариан", "Грин", 9, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Джексон", "Спиделл", 10, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Тиффани", "Вакслер", 10, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Роджер", "Митчел", 11, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Сидер", "Хоуэлл", 11, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Пит", "Мелларк", 12, Sex.MALE),
        new Player(PlayerStatus.ALIVE, "Китнисс", "Эвердин", 12, Sex.FEMALE)
    );

    public final static List<Player> playersFullRandom = List.of(
        new Player(PlayerStatus.DEAD, "Блеск", "Ритчсон", 1, Sex.MALE),
        new Player(PlayerStatus.SLIGHT_INJURY, "Кашмира", "Ритчсон", 1, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Брут", "Ганн", 2, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Энобария", "Голдинг", 2, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Бити", "Литье", 3, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Вайресс", "Кларк", 3, Sex.FEMALE),
        new Player(PlayerStatus.ALIVE, "Финник", "Одэйр", 4, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Мэгз", "Флэнаган", 4, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джеймс", "Логан", 5, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Иветта", "Ли-Санчес", 5, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Бобби", "Джордан", 6, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Дайме", "Кёсслер", 6, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джастин", "Хикс", 7, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Джоанна", "Мейсон", 7, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джон", "Касино", 8, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Цецилия", "Санчез", 8, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Даниэль", "Бернхардт", 9, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Мариан", "Грин", 9, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джексон", "Спиделл", 10, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Тиффани", "Вакслер", 10, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Роджер", "Митчел", 11, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Сидер", "Хоуэлл", 11, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Пит", "Мелларк", 12, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Китнисс", "Эвердин", 12, Sex.FEMALE)
    );

    public final static List<Player> playersOneAlive = List.of(
        new Player(PlayerStatus.DEAD, "Блеск", "Ритчсон", 1, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Кашмира", "Ритчсон", 1, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Брут", "Ганн", 2, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Энобария", "Голдинг", 2, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Бити", "Литье", 3, Sex.MALE),
        new Player(PlayerStatus.MODERATE_INJURY, "Вайресс", "Кларк", 3, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Финник", "Одэйр", 4, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Мэгз", "Флэнаган", 4, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джеймс", "Логан", 5, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Иветта", "Ли-Санчес", 5, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Бобби", "Джордан", 6, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Дайме", "Кёсслер", 6, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джастин", "Хикс", 7, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Джоанна", "Мейсон", 7, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джон", "Касино", 8, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Цецилия", "Санчез", 8, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Даниэль", "Бернхардт", 9, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Мариан", "Грин", 9, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Джексон", "Спиделл", 10, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Тиффани", "Вакслер", 10, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Роджер", "Митчел", 11, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Сидер", "Хоуэлл", 11, Sex.FEMALE),
        new Player(PlayerStatus.DEAD, "Пит", "Мелларк", 12, Sex.MALE),
        new Player(PlayerStatus.DEAD, "Китнисс", "Эвердин", 12, Sex.FEMALE)
    );
    
    public final static List<Item> items = List.of(
        new Item("Аптечка", "Надежный источник восстановления здоровья. Аптечка содержит специальные медицинские препараты, позволяющие оказать первую помощь и восполнить силы", BigDecimal.valueOf(500.0)),
        new Item("Стрелы", "Боеприпасы для лука. Стрелы изготовлены из прочного материала и оснащены острыми наконечниками", BigDecimal.valueOf(700.0)),
        new Item("Лук", "Эффективное оружие для дальнобойных сражений. Прочный лук изготовлен из композитных материалов", BigDecimal.valueOf(1000.0)),
        new Item("Антидот", "Специальный препарат, способный нейтрализовать ядовитые вещества. Антидот может быть жизненно необходим в ситуациях, где яд или отравление угрожают здоровью игрока", BigDecimal.valueOf(1000.0)),
        new Item("Камуфляж", "Камуфляжный костюм. Обеспечивает игроку тактическое преимущество", BigDecimal.valueOf(600.0))
    );
}
