package se.ifmo.ru.web.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlatGetResponseDto {
    private Long id;
    private String name;
    private FlatCoordinatesGetResponsesDto coordinates;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime creationDate;
    private Integer area;
    private Long numberOfRooms;
    private Integer floor;
    private Integer timeToMetroOnFoot;
    private Boolean balcony;
    private String view;
    private FlatHouseGetResponseDto house;
    private Double price;

    @Data
    public static class FlatCoordinatesGetResponsesDto {
        private Integer x;
        private Float y;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlatHouseGetResponseDto {
        private String name;
        private Long year;
        private Integer numberOfFloors;
    }
}
