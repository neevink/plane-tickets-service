package com.lab2.secservice.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;


@Setter
@Getter
@Accessors(chain = true)
public class VehiclesListDTO {
    private List<VehicleDTO> vehiclesGetResponseDtos;
}
