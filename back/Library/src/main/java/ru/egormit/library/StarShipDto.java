package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель передачи starship.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class StarShipDto {

    /**
     * Идентификатор.
     */
    private Long id;

    /**
     * Имя.
     */
    private String name;

    /**
     * Флотн.
     */
    private String fleet;

    /**
     * Координаты.
     */
    private Coordinates coordinates;

    /**
     * Здоровье.
     */
    private Long health;

    /**
     * Количество солдат.
     */
    private Long marinesCount;

    /**
     * ID десетников на корабле.
     */
    List<Long> spaceMarines;

}
