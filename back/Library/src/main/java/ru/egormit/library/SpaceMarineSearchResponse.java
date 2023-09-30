package ru.egormit.library;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель ответа на search запрос.
 *
 * @author Egor Mitrofanov.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SpaceMarineSearchResponse {

    /**
     * Список найденных космодесантников.
     */
    List<SpaceMarineResponse> items;
    Long pageNumber;

}
