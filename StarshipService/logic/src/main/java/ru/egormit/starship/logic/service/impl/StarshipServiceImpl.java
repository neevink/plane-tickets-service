package ru.egormit.starship.logic.service.impl;

import org.jboss.ejb3.annotation.Pool;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;
import ru.egormit.library.StarShip;
import ru.egormit.library.StarShipDto;
import ru.egormit.library.StarShipRequest;
import ru.egormit.starship.logic.error.ErrorDescriptions;
import ru.egormit.starship.logic.utils.ModelMapper;
import ru.egormit.starship.logic.domain.StarshipRepository;
import ru.egormit.starship.logic.service.StarshipService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса работы с starship.
 *
 * @author Egor Mitrofanov.
 */
@Stateless
@Pool(value = "starshipServicePool")
public class StarshipServiceImpl implements StarshipService {
    private static final String MARINES_URL = "http://haproxy:10000/spacemarines";

    /**
     * {@link StarshipRepository}.
     */
    @Inject
    private StarshipRepository starshipRepository;

    /**
     * {@link ModelMapper}.
     */
    @Inject
    private ModelMapper modelMapper;

    /**
     * Создание корабля
     *
     * @param request модель корабля
     */
    @Override
    public void createStarship(StarShipRequest request) {
        StarShip starShip = new StarShip();
        starShip.setName(request.getName());
        starShip.setFleet(request.getFleet());
        starShip.setCoordinateX(request.getCoordinates().getX());
        starShip.setCoordinateY(request.getCoordinates().getY());

        starshipRepository.save(starShip);
    }

    /**
     * Получение списка кораблей
     *
     * @return список всех кораблей
     */
    @Override
    public List<StarShipDto> getAllStarships() {
        return starshipRepository.findAll()
                .stream()
                .map(modelMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Добавление десантника к кораблю
     *
     * @param spaceMarineId идентификатор десантника
     * @param starShipId    идентификатор корабля
     */
    @Override
    public void addMarineToStarship(Long spaceMarineId, Long starShipId) {
        SpaceMarineResponse spaceMarine = performGetMarineById(spaceMarineId);
        if (spaceMarine == null) {
            throw ErrorDescriptions.MARINE_NOT_FOUND.exception();
        }
        if (spaceMarine.getStarShipId() != null) {
            throw ErrorDescriptions.SPACEMACS_IS_BUSY.exception();
        }

        SpaceMarineUpdateRequest request = modelMapper.map(spaceMarine);

        StarShip starShip = starshipRepository.findById(starShipId);
        if (starShip == null) {
            throw ErrorDescriptions.STARSHIP_NOT_FOUND.exception();
        }

        request.setStarShip(modelMapper.map(starShip));

        performUpdateMarine(request);
    }

    /**
     * Выгрузка всех десантников с корабля
     *
     * @param starShipId идентификатор корабля
     */
    @Override
    public void cleanStarship(Long starShipId) {
        StarShip starShip = starshipRepository.findById(starShipId);
        if (starShip == null) {
            throw ErrorDescriptions.STARSHIP_NOT_FOUND.exception();
        }

        if (starShip.getSpaceMarines() == null || starShip.getSpaceMarines().isEmpty()) {
            throw ErrorDescriptions.STARSHIP_IS_EMPTY.exception();
        }

        for (SpaceMarine spaceMarine : starShip.getSpaceMarines()) {
            SpaceMarineResponse spaceMarineDto = performGetMarineById(spaceMarine.getId());
            SpaceMarineUpdateRequest request = modelMapper.map(spaceMarineDto);

            request.setStarShip(null);
            performUpdateMarine(request);
        }
    }

    private SpaceMarineResponse performGetMarineById(Long id) {
        return ClientBuilder.newClient()
                .target(MARINES_URL)
                .path("/{id}")
                .resolveTemplate("id", id)
                .request(MediaType.APPLICATION_JSON)
                .get(SpaceMarineResponse.class);
    }

    private void performUpdateMarine(SpaceMarineUpdateRequest request) {
        ClientBuilder.newClient()
                .target(MARINES_URL)
                .path("/{id}")
                .resolveTemplate("id", request.getId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(request));
    }

}
