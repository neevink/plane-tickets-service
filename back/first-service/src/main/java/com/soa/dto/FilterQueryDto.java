package com.soa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties
public class FilterQueryDto {
    private List<Integer> id = null;
    private List<Integer> coordinatesX = null;
    private List<Double> coordinatesY = null;
    private List<String> disciplineName = null;
    private List<Integer> disciplineSelfStudyHours = null;
    private List<String> name = null;
    private List<Integer> minimalPoint = null;
    private List<Difficulty> difficulty = null;
    private Boolean sortAsc;
    private List<String> sort = null;
    private Integer offset;
    private Integer limit;
}
