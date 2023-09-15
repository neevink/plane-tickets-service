package se.ifmo.ru.firstservice.storage.repostitory.api;

import org.springframework.stereotype.Repository;
import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.Filter;
import se.ifmo.ru.firstservice.storage.model.FlatEntity;
import se.ifmo.ru.firstservice.storage.model.Page;
import se.ifmo.ru.firstservice.storage.model.Sort;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlatRepository {
    FlatEntity findById(long id);

    FlatEntity save(FlatEntity entity);

    boolean deleteById(long id);

    Page<FlatEntity> getSortedAndFilteredPage(List<Sort> sortList, List<Filter> filtersList, Integer page, Integer size);

    boolean deleteByView(View view);

    double averageTimeToMetro();

    List<String> getUniqueView();
}
