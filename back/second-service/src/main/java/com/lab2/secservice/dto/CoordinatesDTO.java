package com.lab2.secservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Setter
@Getter
@Accessors(chain = true)
public class CoordinatesDTO {
    private BigDecimal x;
    private BigDecimal y;
}