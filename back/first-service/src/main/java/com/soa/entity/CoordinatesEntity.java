package com.soa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class CoordinatesEntity {
    private int x;
    private Double y;
}
