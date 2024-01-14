package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FilterCriteria {
    private String key;
    private String operation;
    private Object value;
}
