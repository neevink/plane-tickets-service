package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Модель координат.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Coordinates implements Serializable {

    /**
     * Координата X.
     */
    private Integer x;

    /**
     * Координата Y.
     */
    private Long y;

}
