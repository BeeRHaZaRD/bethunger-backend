package com.hg.bethunger.model.init;

import com.hg.bethunger.model.Item;
import com.hg.bethunger.model.Player;
import com.hg.bethunger.model.enums.Sex;

import java.math.BigDecimal;
import java.util.List;

public class InitData {
    public final static List<Player> players = List.of(
        new Player("Блеск", "Ритчсон", 1, Sex.MALE),
        new Player("Кашмира", "Ритчсон", 1, Sex.FEMALE),
//        new Player("Брут", "Ганн", 2, Sex.MALE),
        new Player("Энобария", "Голдинг", 2, Sex.FEMALE),
        new Player("Бити", "Литье", 3, Sex.MALE),
//        new Player("Вайресс", "Кларк", 3, Sex.FEMALE),
        new Player("Финник", "Одэйр", 4, Sex.MALE),
        new Player("Мэгз", "Флэнаган", 4, Sex.FEMALE),
        new Player("Джеймс", "Логан", 5, Sex.MALE),
        new Player("Иветта", "Ли-Санчес", 5, Sex.FEMALE),
//        new Player("Бобби", "Джордан", 6, Sex.MALE),
//        new Player("Дайме", "Кёсслер", 6, Sex.FEMALE),
        new Player("Джастин", "Хикс", 7, Sex.MALE),
        new Player("Джоанна", "Мейсон", 7, Sex.FEMALE),
        new Player("Джон", "Касино", 8, Sex.MALE),
        new Player("Цецилия", "Санчез", 8, Sex.FEMALE),
        new Player("Даниэль", "Бернхардт", 9, Sex.MALE),
        new Player("Мариан", "Грин", 9, Sex.FEMALE),
        new Player("Джексон", "Спиделл", 10, Sex.MALE),
        new Player("Тиффани", "Вакслер", 10, Sex.FEMALE),
        new Player("Роджер", "Митчел", 11, Sex.MALE),
        new Player("Сидер", "Хоуэлл", 11, Sex.FEMALE),
        new Player("Пит", "Мелларк", 12, Sex.MALE),
        new Player("Китнисс", "Эвердин", 12, Sex.FEMALE)
    );

    public final static List<Item> items = List.of(
        new Item("Аптечка", "Надежный источник восстановления здоровья. Аптечка содержит специальные медицинские препараты, позволяющие оказать первую помощь и восполнить силы", BigDecimal.valueOf(500.0)),
        new Item("Стрелы", "Боеприпасы для лука. Стрелы изготовлены из прочного материала и оснащены острыми наконечниками", BigDecimal.valueOf(700.0)),
        new Item("Лук", "Эффективное оружие для дальнобойных сражений. Прочный лук изготовлен из композитных материалов", BigDecimal.valueOf(1000.0)),
        new Item("Антидот", "Специальный препарат, способный нейтрализовать ядовитые вещества. Антидот может быть жизненно необходим в ситуациях, где яд или отравление угрожают здоровью игрока", BigDecimal.valueOf(1000.0)),
        new Item("Камуфляж", "Камуфляжный костюм. Обеспечивает игроку тактическое преимущество", BigDecimal.valueOf(600.0))
    );
}
