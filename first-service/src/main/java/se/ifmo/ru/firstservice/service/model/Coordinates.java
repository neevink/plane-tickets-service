package se.ifmo.ru.firstservice.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    private Integer x; //Поле не может быть null
    private Float y; //Поле не может быть null
}
