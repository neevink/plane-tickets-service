package se.ifmo.ru.firstservice.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class House {
    private String name; //Поле может быть null
    private Long year; //Значение поля должно быть больше 0
    private Integer numberOfFloors; //Значение поля должно быть больше 0
}
