package com.soa.service.db;

import com.soa.dto.FilterQueryDto;
import com.soa.entity.GroupsCount;
import com.soa.entity.LabWorkEntity;
import com.soa.repository.LabWorkRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class LabWorkDbService {
    private LabWorkRepository labWorkRepository;

    @Transactional
    public LabWorkEntity save(LabWorkEntity labWorkEntity) {
        return labWorkRepository.save(labWorkEntity);
    }

    @Transactional
    public List<LabWorkEntity> saveAll(List<LabWorkEntity> labWorks) {
        return labWorkRepository.saveAll(labWorks);
    }

    @Transactional(readOnly = true)
    public Optional<LabWorkEntity> findById(Integer id) {
        return labWorkRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Integer getSum() {
        return labWorkRepository.getSumOfMinimalPoint();
    }

    @Transactional(readOnly = true)
    public List<GroupsCount> getGroupedByCoordinates() {
        return labWorkRepository.getGroupedByCoordinates();
    }

    @Transactional(readOnly = true)
    public List<Integer> getDistinctByMinimalPoint() {
        return labWorkRepository.getDistinctByMinimalPoint();
    }

    @Transactional(readOnly = true)
    public List<LabWorkEntity> getLabWorksWithFiltering(FilterQueryDto dto, Pageable pageable) {
        return labWorkRepository.getLabWorksWithFiltering(dto, pageable);
    }

    @Transactional(readOnly = true)
    public List<LabWorkEntity> getHardestLabWorks() {
        return labWorkRepository.getHardestLabWorks();
    }

    @Transactional(readOnly = true)
    public List<LabWorkEntity> suggest(String name, int limit) {
        return labWorkRepository.suggest(name, limit);
    }

    @Transactional
    public void deleteById(Integer id) {
        labWorkRepository.deleteById(id);
    }
}
