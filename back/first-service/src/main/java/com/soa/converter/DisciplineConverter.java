package com.soa.converter;

import com.soa.dto.DisciplineDto;
import com.soa.entity.DisciplineEntity;
import org.springframework.stereotype.Component;

@Component
public class DisciplineConverter {
    public DisciplineEntity convertToEntity(DisciplineDto dto) {
        DisciplineEntity entity = new DisciplineEntity();
        entity
                .setName(dto.getName())
                .setSelfStudyHours(dto.getSelfStudyHours());
        return entity;
    }

    public DisciplineDto convertToDto(DisciplineEntity entity) {
        DisciplineDto dto = new DisciplineDto();
        dto
                .setId(entity.getId())
                .setName(entity.getName())
                .setSelfStudyHours(entity.getSelfStudyHours());
        return dto;
    }
}
