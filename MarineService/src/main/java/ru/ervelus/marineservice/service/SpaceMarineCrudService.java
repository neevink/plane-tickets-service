package ru.ervelus.marineservice.service;

import ru.egormit.library.PageDto;
import ru.egormit.library.SpaceMarineCreateRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;

public interface SpaceMarineCrudService {
    void createSpaceMarine(SpaceMarineCreateRequest request);

    void updateSpaceMarine(Long id, SpaceMarineUpdateRequest request);

    SpaceMarineSearchResponse getAllSpaceMarines(PageDto pageDto);

    SpaceMarineResponse getSpaceMarineById(Long id);

    void deleteSpaceMarine(Long id);
}
