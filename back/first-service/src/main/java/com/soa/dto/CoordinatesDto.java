package com.soa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Accessors(chain = true)
public class CoordinatesDto {
    @Min(value = -569)
    private int x;
    @NotBlank
    @Min(value = -302)
    private Double y;
}
