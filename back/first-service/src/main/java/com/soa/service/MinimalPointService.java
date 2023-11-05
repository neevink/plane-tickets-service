package com.soa.service;

import com.soa.service.db.LabWorkDbService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MinimalPointService {
    private LabWorkDbService labWorkDbService;

    public Integer getSum() {
        return labWorkDbService.getSum();
    }

    public List<Integer> getDistinctByMinimalPoint() {
        return labWorkDbService.getDistinctByMinimalPoint();
    }
}
