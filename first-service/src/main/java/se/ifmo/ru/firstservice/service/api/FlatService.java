package se.ifmo.ru.firstservice.service.api;

import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.Page;
import se.ifmo.ru.firstservice.web.model.request.FlatAddOrUpdateRequestDto;

import java.util.List;

public interface FlatService {
    Page<Flat> getFlats(List<String> sortsList, List<String> filtersList, Integer page, Integer pageSize);

    Flat getFlat(long id);

    Flat updateFlat(long id, FlatAddOrUpdateRequestDto requestDto);

    Flat addFlat(FlatAddOrUpdateRequestDto requestDto);

    boolean deleteFlat(long id);

    boolean deleteOneFlatByView(View view);

    double getFlatsAverageTimeToMetroOnFoot();

    List<String> getUniqueFlatsView();
}
