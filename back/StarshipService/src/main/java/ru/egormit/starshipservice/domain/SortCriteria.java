package ru.egormit.starshipservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SortCriteria {
    private String key;
    private Boolean ascending;
}
