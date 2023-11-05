package com.soa.service;

import com.soa.converter.DisciplineConverter;
import com.soa.dto.DisciplineDto;
import com.soa.service.db.DisciplineDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DisciplineService {
    private final DisciplineDbService disciplineDbService;
    private final DisciplineConverter disciplineConverter;

    public List<DisciplineDto> suggest(String name, int limit) {
        return disciplineDbService.suggest(name, limit).stream().map(disciplineConverter::convertToDto).toList();
    }

}
