package com.lab2.secservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Setter
@Getter
@Accessors(chain = true)
public class VehicleDTO {
    private Integer id;
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotNull
    private CoordinatesDTO coordinates;
    @NotNull
    private BigDecimal enginePower;
    @NotNull
    private VehicleType type;
    @NotNull
    private FuelType fuelType;
    @NotNull
    private BigDecimal numberOfWheels;
}

enum FuelType {
    ELECTRICITY,
    DIESEL,
    NUCLEAR
}
enum VehicleType {
    HELICOPTER,
    DRONE,
    SHIP,
    MOTORCYCLE,
    HOVERBOARD
}