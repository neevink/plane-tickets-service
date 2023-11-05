package com.example.extraservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CoordinatesDto {
    private Integer x;
    private Float y;
}
