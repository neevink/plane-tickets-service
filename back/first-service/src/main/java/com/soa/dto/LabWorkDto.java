package com.soa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Setter
@Getter
@Accessors(chain = true)
public class LabWorkDto {
    @JsonIgnoreProperties
    private Integer id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
    @NotNull
    private CoordinatesDto coordinates;
    private Instant creationDate;
    @Min(value = 0, message = "Минимальная оценка должна быть больше 0")
    private Integer minimalPoint;
    private Difficulty difficulty;
    @NotNull
    private DisciplineDto discipline;
    @JsonIgnoreProperties
    private int stepsCount;
}
