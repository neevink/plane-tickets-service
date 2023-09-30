package ru.egormit.starshipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.egormit.library.StarShipDto;
import ru.egormit.library.StarShipRequest;
import ru.egormit.starshipservice.common.Endpoints;
import ru.egormit.starshipservice.service.StarshipService;

import java.util.List;

/**
 * Обработчик запросов.
 *
 * @author Egor Mitrofanov.
 */
@RestController
@RequiredArgsConstructor
public class StarshipController {

    /**
     * {@link StarshipService}.
     */
    private final StarshipService starshipService;

    /**
     * Создание корабля.
     *
     * @param request тело запроса.
     */
    @PostMapping(value = Endpoints.CREATE_STARSHIP)
    public ResponseEntity<Object> getStarship(@RequestBody StarShipRequest request) {
        starshipService.createStarship(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Получение списка кораблей.
     *
     * @return список кораблей.
     */
    @GetMapping(value = Endpoints.GET_ALL_STARSHIPS)
    public ResponseEntity<List<StarShipDto>> getAllStarships() {
        return new ResponseEntity<>(starshipService.getAllStarships(), HttpStatus.OK);
    }

    /**
     * Добавление на корабль
     *
     * @param spacemarineId идентификатор десантника
     * @param starshipId    идентификатор корабля
     */
    @PutMapping(value = Endpoints.ADD_MARINE_TO_STARSHIP)
    public ResponseEntity<Object> addMarineToStarship(@PathVariable("spacemarine-id") Long spacemarineId,
                                                      @PathVariable("starship-id") Long starshipId) {
        starshipService.addMarineToStarship(spacemarineId, starshipId);
        return ResponseEntity.ok().build();
    }

    /**
     * Выгрузка всех с корабля
     *
     * @param id идентификатор корабля
     */
    @PostMapping(value = Endpoints.CLEAN_STARSHIP)
    public ResponseEntity<Object> cleanStarship(@PathVariable Long id) {
        starshipService.cleanStarship(id);
        return ResponseEntity.ok().build();
    }

}
