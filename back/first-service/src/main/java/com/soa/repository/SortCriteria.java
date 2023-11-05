package com.soa.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SortCriteria {
    private String key;
    private Boolean ascending;
}
