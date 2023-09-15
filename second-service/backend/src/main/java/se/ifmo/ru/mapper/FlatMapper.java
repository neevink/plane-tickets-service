package se.ifmo.ru.mapper;

import org.mapstruct.Mapper;
import se.ifmo.ru.web.model.response.FlatGetResponseDto;
import se.ifmo.ru.service.model.Flat;

import java.util.List;

@Mapper
public interface FlatMapper {
    FlatGetResponseDto toDto(Flat source);

    List<FlatGetResponseDto> toGetResponseDtoList(List<Flat> source);
}
