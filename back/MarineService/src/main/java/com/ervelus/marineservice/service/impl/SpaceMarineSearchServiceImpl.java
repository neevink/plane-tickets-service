package com.ervelus.marineservice.service.impl;

import com.ervelus.marineservice.repository.SpaceMarineSearchRepository;
import com.ervelus.marineservice.service.SpaceMarineSearchService;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.SpaceMarineFilterRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.egormit.library.enums.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class SpaceMarineSearchServiceImpl implements SpaceMarineSearchService {
    @Inject
    private SpaceMarineSearchRepository repository;

    @Override
    public SpaceMarineSearchResponse findAllSpaceMarineByFilter(SpaceMarineFilterRequest request) {
        HashMap<String, String> fieldToVal = new HashMap<>();
        if (request.getName() != null) fieldToVal.put("name", request.getName());
        if (request.getCoordinates() != null) {
            fieldToVal.put("coordinateX", request.getCoordinates().getX().toString());
            fieldToVal.put("coordinateY", request.getCoordinates().getY().toString());
        }
        if (request.getCreationDate() != null) fieldToVal.put("creationDate", request.getCreationDate().toString());
        if (request.getHealth() != null) fieldToVal.put("health", request.getHealth().toString());
        if (request.getCategory() != null) fieldToVal.put("category", request.getCategory());
        if (request.getWeaponType() != null) fieldToVal.put("weaponType", request.getWeaponType());
        if (request.getMeleeWeapon() != null) fieldToVal.put("meleeWeapon", request.getMeleeWeapon());
        List<SpaceMarine> marines;
        if (request.getSortBy() != null) {
            marines = (repository.getAllMatchingFieldsSortedPageable(
                    fieldToVal,
                    SortByType.valueOf(request.getSortBy()),
                    SortOrder.valueOf(request.getOrder().toUpperCase()),
                    request.getPage(),
                    request.getLimit()));
        } else marines = repository.getAllMatchingFieldsPageable(fieldToVal, request.getPage(), request.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    @Override
    public SpaceMarineSearchResponse findAllSpaceMarineByName(SpaceMarineFilterRequest request) {
        List<SpaceMarine> marines = repository.getAllByNamePageable(request.getName(), request.getPage(), request.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    @Override
    public SpaceMarineSearchResponse findAllSpaceMarineWithHealthGreaterThan(SpaceMarineFilterRequest request) {
        List<SpaceMarine> marines = repository.getAllByHealthGreaterPageable(request.getHealth(), request.getPage(), request.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    private Long countPages(Integer size, Integer page, Integer limit){
        if (size < limit)
            return Long.valueOf(page);
        else return (long) (page + 1);
    }
}
