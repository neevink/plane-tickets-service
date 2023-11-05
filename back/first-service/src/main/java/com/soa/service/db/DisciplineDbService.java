package com.soa.service.db;

import com.soa.entity.DisciplineEntity;
import com.soa.repository.DisciplineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DisciplineDbService {
    private final DisciplineRepository disciplineRepository;

    @Transactional(readOnly = true)
    public List<DisciplineEntity> suggest(String name, int limit) {
        return disciplineRepository.suggest(name, limit);
    }

    @Transactional(readOnly = true)
    public Optional<DisciplineEntity> findById(Integer id) {
        return disciplineRepository.findById(id);
    }

}
