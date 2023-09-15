package se.ifmo.ru.firstservice.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AverageTimeMetroDto {
    private int averageTimeMetro;
}
