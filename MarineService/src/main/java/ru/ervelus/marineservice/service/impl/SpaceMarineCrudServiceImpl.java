package ru.ervelus.marineservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.egormit.library.PageDto;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.SpaceMarineCreateRequest;
import ru.egormit.library.SpaceMarineResponse;
import ru.egormit.library.SpaceMarineSearchResponse;
import ru.egormit.library.SpaceMarineUpdateRequest;
import ru.ervelus.marineservice.converter.SpaceMarineConverter;
import ru.ervelus.marineservice.repository.SpaceMarineRepository;
import ru.ervelus.marineservice.service.SpaceMarineCrudService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceMarineCrudServiceImpl implements SpaceMarineCrudService {
    private final SpaceMarineRepository repository;
    private final SpaceMarineConverter converter;

    @Override
    public void createSpaceMarine(SpaceMarineCreateRequest request) {
        SpaceMarine spaceMarine = new SpaceMarine();
        ZonedDateTime time = ZonedDateTime.now();
        spaceMarine.setCreationDate(time.with(LocalTime.of(0, 0, 0, 0)));
        repository.save(converter.createRequestToEntity(request, spaceMarine));
    }

    @Override
    public void updateSpaceMarine(Long id, SpaceMarineUpdateRequest request) {
        SpaceMarine spaceMarine = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.save(converter.updateRequestToEntity(request, spaceMarine));
    }

    @Override
    public SpaceMarineSearchResponse getAllSpaceMarines(PageDto pageDto) {
        List<SpaceMarine> marines = repository.getAllPageable(pageDto.getPage(), pageDto.getLimit());
        List<SpaceMarineResponse> responseList = new ArrayList<>();
        for (SpaceMarine marine : marines) {
            SpaceMarineResponse response = new SpaceMarineResponse();
            responseList.add(converter.entityToResponse(marine, response));
        }
        return SpaceMarineSearchResponse.of(responseList, countPages(pageDto.getLimit()));
    }

    @Override
    public SpaceMarineResponse getSpaceMarineById(Long id) {
        SpaceMarine spaceMarine = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        SpaceMarineResponse response = new SpaceMarineResponse();
        return converter.entityToResponse(spaceMarine, response);
    }

    @Override
    public void deleteSpaceMarine(Long id) {
        repository.deleteById(id);
    }

    private Long countPages(Integer limit) {
        return Double.valueOf(Math.ceil((double) repository.count() / limit.floatValue())).longValue();
    }
}
