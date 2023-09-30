package com.ervelus.marineservice.service;

import ru.egormit.library.SpaceMarineFilterRequest;
import ru.egormit.library.SpaceMarineSearchResponse;

import javax.ejb.Local;

@Local
public interface SpaceMarineSearchService {
    SpaceMarineSearchResponse findAllSpaceMarineByFilter(SpaceMarineFilterRequest request);

    SpaceMarineSearchResponse findAllSpaceMarineByName(SpaceMarineFilterRequest request);

    SpaceMarineSearchResponse findAllSpaceMarineWithHealthGreaterThan(SpaceMarineFilterRequest request);
}
