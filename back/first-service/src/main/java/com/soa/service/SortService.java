package com.soa.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SortService {
    public Sort.Direction getSort(boolean sortAsc) {
        return sortAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
