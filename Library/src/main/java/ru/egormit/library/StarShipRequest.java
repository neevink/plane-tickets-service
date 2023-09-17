package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Модель запрса на саоздание Starship.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class StarShipRequest implements Serializable {

    /**
     * Имя.
     */
    private String name;

    /**
     * Флот.
     */
    private String fleet;

    /**
     * Координаты.
     */
    private Coordinates coordinates;

}
