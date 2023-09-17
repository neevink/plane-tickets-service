package ru.ervelus.marineservice.converter;

import org.springframework.stereotype.Service;
import ru.egormit.library.Coordinates;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.SpaceMarineCreateRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;
import ru.egormit.library.StarShip;
import ru.egormit.library.StarShipDto;

@Service
public class SpaceMarineConverter {
    public SpaceMarineResponse entityToResponse(SpaceMarine spaceMarine, SpaceMarineResponse response) {
        response.setId(spaceMarine.getId());
        response.setName(spaceMarine.getName());
        response.setCoordinates(Coordinates.of(spaceMarine.getCoordinateX(), spaceMarine.getCoordinateY()));
        response.setHealth(spaceMarine.getHealth());
        response.setCategory(spaceMarine.getCategory());
        response.setCreationDate(spaceMarine.getCreationDate());
        response.setWeaponType(spaceMarine.getWeaponType());
        response.setMeleeWeapon(spaceMarine.getMeleeWeapon());
        if (spaceMarine.getStarShip() != null)
            response.setStarShipId(spaceMarine.getStarShip().getId());
        return response;
    }

    public SpaceMarine createRequestToEntity(SpaceMarineCreateRequest request, SpaceMarine spaceMarine) {
        spaceMarine.setName(request.getName());
        spaceMarine.setCoordinateX(request.getCoordinates().getX());
        spaceMarine.setCoordinateY(request.getCoordinates().getY());
        spaceMarine.setHealth(request.getHealth());
        spaceMarine.setCategory(request.getCategory());
        spaceMarine.setWeaponType(request.getWeaponType());
        spaceMarine.setMeleeWeapon(request.getMeleeWeapon());
        return spaceMarine;
    }

    public SpaceMarine updateRequestToEntity(SpaceMarineUpdateRequest request, SpaceMarine spaceMarine) {
        spaceMarine.setName(request.getName());
        spaceMarine.setCoordinateX(request.getCoordinates().getX());
        spaceMarine.setCoordinateY(request.getCoordinates().getY());
        spaceMarine.setHealth(request.getHealth());
        spaceMarine.setCategory(request.getCategory());
        spaceMarine.setWeaponType(request.getWeaponType());
        spaceMarine.setMeleeWeapon(request.getMeleeWeapon());
        if (request.getStarShip() != null)
            spaceMarine.setStarShip(starShipEntityFromDto(request.getStarShip()));
        else spaceMarine.setStarShip(null);
        return spaceMarine;
    }

    public StarShip starShipEntityFromDto(StarShipDto dto) {
        StarShip starShip = new StarShip();
        starShip.setId(dto.getId());
        starShip.setCoordinateX(dto.getCoordinates().getX());
        starShip.setCoordinateY(dto.getCoordinates().getY());
        starShip.setFleet(dto.getFleet());
        starShip.setName(dto.getName());
        return starShip;
    }
}
