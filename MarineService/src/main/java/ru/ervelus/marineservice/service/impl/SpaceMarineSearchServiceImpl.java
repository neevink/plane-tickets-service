package ru.ervelus.marineservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.SpaceMarineFilterRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.egormit.library.enums.SortByType;
import ru.egormit.library.enums.SortOrder;
import ru.ervelus.marineservice.converter.SpaceMarineConverter;
import ru.ervelus.marineservice.repository.SpaceMarineRepository;
import ru.ervelus.marineservice.service.SpaceMarineSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceMarineSearchServiceImpl implements SpaceMarineSearchService {
    private final SpaceMarineRepository repository;
    private final SpaceMarineConverter converter;

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
            responseList.add(converter.entityToResponse(marine, response));
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    @Override
    public SpaceMarineSearchResponse findAllSpaceMarineByName(SpaceMarineFilterRequest request) {
        List<SpaceMarine> marines = repository.getAllByNamePageable(request.getName(), request.getPage(), request.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
            responseList.add(converter.entityToResponse(marine, response));
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    @Override
    public SpaceMarineSearchResponse findAllSpaceMarineWithHealthGreaterThan(SpaceMarineFilterRequest request) {
        List<SpaceMarine> marines = repository.getAllByHealthGreaterPageable(request.getHealth(), request.getPage(), request.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
            responseList.add(converter.entityToResponse(marine, response));
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(responseList.size(), request.getPage(), request.getLimit()));
    }

    private Long countPages(Integer size, Integer page, Integer limit) {
        if (size < limit)
            return Long.valueOf(page);
        else return (long) (page + 1);
    }
}
