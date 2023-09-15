package se.ifmo.ru.firstservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import se.ifmo.ru.firstservice.service.model.Coordinates;
import se.ifmo.ru.firstservice.service.model.Flat;
import se.ifmo.ru.firstservice.service.model.House;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.FlatEntity;
import se.ifmo.ru.firstservice.web.model.request.FlatAddOrUpdateRequestDto;
import se.ifmo.ru.firstservice.web.model.response.FlatGetResponseDto;

import java.util.List;

@Mapper
public interface FlatMapper {
    FlatGetResponseDto toDto(Flat source);
    List<FlatGetResponseDto> toGetResponseDtoList(List<Flat> source);

    @Mapping(target = "view", qualifiedByName = "viewConverter")
    FlatEntity fromFlatRequestDto(FlatAddOrUpdateRequestDto requestDto);

    default Flat fromEntity(FlatEntity entity){
        if (entity == null){
            return null;
        }

        Flat.FlatBuilder flat = Flat.builder();

        flat.id(entity.getId());
        flat.name(entity.getName());
        flat.coordinates(Coordinates.builder()
                .x(entity.getCoordinates().getX())
                .y(entity.getCoordinates().getY())
                .build()
        );
        flat.creationDate(entity.getCreationDate());
        flat.area(entity.getArea());
        flat.numberOfRooms(entity.getNumberOfRooms());
        flat.floor(entity.getFloor());
        flat.timeToMetroOnFoot(entity.getTimeToMetroOnFoot());
        flat.balcony(entity.getBalcony());
        flat.view(entity.getView());

        if (entity.getHouse() != null) {
            flat.house(House.builder()
                    .name(entity.getHouse().getName())
                    .year(entity.getHouse().getYear())
                    .numberOfFloors(entity.getHouse().getNumberOfFloors())
                    .build()
            );
        }
        flat.price(entity.getPrice());

        return flat.build();
    }

    List<Flat> fromEntityList(List<FlatEntity> entity);

    default String from(View view){
        return view.toString();
    }

    @Named(value = "viewConverter")
    default View convertView(String view){
        return View.fromValue(view);
    }
}
