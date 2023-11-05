package com.soa.converter;

import com.soa.dto.LabWorkDto;
import com.soa.entity.LabWorkEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LabWorkConverter {
    private final CoordinatesConverter coordinatesConverter;
    private final DisciplineConverter disciplineConverter;

    public LabWorkEntity convertToEntity(LabWorkDto dto) {
        LabWorkEntity entity = new LabWorkEntity();
        entity.setId(dto.getId())
                .setName(dto.getName())
                .setCoordinates(coordinatesConverter.convertToEntity(dto.getCoordinates()))
                .setCreationDate(dto.getCreationDate())
                .setMinimalPoint(dto.getMinimalPoint())
                .setDifficulty(dto.getDifficulty())
                .setDiscipline(disciplineConverter.convertToEntity(dto.getDiscipline()));
        return entity;
    }

    public LabWorkDto convertToDto(LabWorkEntity entity) {
        LabWorkDto dto = new LabWorkDto();
        dto
                .setId(entity.getId())
                .setName(entity.getName())
                .setCoordinates(coordinatesConverter.convertToDto(entity.getCoordinates()))
                .setCreationDate(entity.getCreationDate())
                .setMinimalPoint(entity.getMinimalPoint())
                .setDifficulty(entity.getDifficulty())
                .setDiscipline(disciplineConverter.convertToDto(entity.getDiscipline()));
        return dto;
    }
}
