package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egormit.library.enums.AstartesCategory;
import ru.egormit.library.enums.MeleeWeapon;
import ru.egormit.library.enums.Weapon;

import java.time.ZonedDateTime;

/**
 * Модель Space Marine.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SpaceMarineResponse {

    /**
     * Идентификатор.
     */
    private Long id;

    /**
     * Имя.
     */
    private String name;

    /**
     * Координаты.
     */
    private Coordinates coordinates;

    /**
     * Дата создания.
     */
    private ZonedDateTime creationDate;

    /**
     * Количество здоровья.
     */
    private Long health;

    /**
     * Класс персонажа.
     */
    private AstartesCategory category;

    /**
     * Тип оружия.
     */
    private Weapon weaponType;

    /**
     * Тип оружия ближнего боя.
     */
    private MeleeWeapon meleeWeapon;

    /**
     * Корабль.
     */
    private Long starShipId;

}
