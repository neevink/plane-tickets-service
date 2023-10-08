package ru.itmo.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель координат.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Coordinates {

    /**
     * Координата X.
     */
    private Long x;

    /**
     * Координата Y.
     */
    private Integer y;

}
