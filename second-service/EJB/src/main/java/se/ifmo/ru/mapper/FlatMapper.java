package se.ifmo.ru.mapper;

import org.mapstruct.Mapper;
import se.ifmo.ru.external.model.RestClientFlat;
import se.ifmo.ru.service.model.Flat;
import se.ifmo.ru.service.model.View;

import java.util.List;

@Mapper
public interface FlatMapper {

    Flat fromRestClient(RestClientFlat restClientFlat);

    List<Flat> fromRestClient(List<RestClientFlat> restClientFlat);

    default String from(View view){
        return view.toString();
    }

    default View from (String view){
        return View.fromValue(view);
    }
}
