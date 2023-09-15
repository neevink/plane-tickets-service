package se.ifmo.ru.firstservice.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatGetResponseDto {
    @NotNull(message = "id - не может быть пустым!")
    @Size(min = 1, message = "id - должен быть больше 0!")
    private Long id;
    @NotNull(message = "name - не может быть пустым!")
    @NotBlank(message = "name - не может быть пустым!")
    private String name;
    @NotNull(message = "coordinates - не может быть пустым!")
    private FlatCoordinatesGetResponsesDto coordinates;
    @NotNull(message = "creationDate - не может быть пустым!")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime creationDate;
    @Size(min = 1, message = "area - должен быть больше 0!")
    private Integer area;
    @Size(min = 1, message = "numberOfRooms - должен быть больше 0!")
    private Long numberOfRooms;
    @Size(min = 1, message = "floor - должен быть больше 0!")
    private Integer floor;
    @Size(min = 1, message = "timeToMetroOnFoot - должен быть больше 0!")
    private Integer timeToMetroOnFoot;
    @NotNull
    private Boolean balcony;
    private String view;
    private FlatHouseGetResponseDto house;
    @NotNull
    @Size(min = 1, message = "price - должен быть больше 0!")
    private Double price;

    @Data
    public static class FlatCoordinatesGetResponsesDto {
        private Integer x;
        private Float y;
    }

    @Data
    public static class FlatHouseGetResponseDto {
        private String name;
        private Long year;
        private Integer numberOfFloors;
    }
}
