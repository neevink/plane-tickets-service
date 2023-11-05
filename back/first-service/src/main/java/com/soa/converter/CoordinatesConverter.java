package com.soa.converter;

import com.soa.dto.CoordinatesDto;
import com.soa.entity.CoordinatesEntity;
import org.springframework.stereotype.Component;

@Component
public class CoordinatesConverter {
    public CoordinatesEntity convertToEntity(CoordinatesDto dto) {
        CoordinatesEntity entity = new CoordinatesEntity();
        entity
                .setX(dto.getX())
                .setY(dto.getY());
        return entity;
    }

    public CoordinatesDto convertToDto(CoordinatesEntity entity) {
        CoordinatesDto dto = new CoordinatesDto();
        dto
                .setX(entity.getX())
                .setY(entity.getY());
        return dto;
    }
}
