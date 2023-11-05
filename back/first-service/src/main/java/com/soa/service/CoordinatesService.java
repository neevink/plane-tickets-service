package com.soa.service;

import com.soa.entity.GroupsCount;
import com.soa.service.db.LabWorkDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CoordinatesService {
    private LabWorkDbService labWorkDbService;

    public List<GroupsCount> getGroupedByCoordinates() {
        return labWorkDbService.getGroupedByCoordinates();
    }
}
