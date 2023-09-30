package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.egormit.library.enums.AstartesCategory;
import ru.egormit.library.enums.MeleeWeapon;
import ru.egormit.library.enums.Weapon;

/**
 * Модель запроса на создание Space Marine.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SpaceMarineCreateRequest {

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

}
