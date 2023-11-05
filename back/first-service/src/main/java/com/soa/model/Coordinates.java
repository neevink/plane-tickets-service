package com.soa.model;

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
    private Integer x;

    /**
     * Координата Y.
     */
    private Float y;

}
